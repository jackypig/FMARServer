package services;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import core.Global;
import models.Blob;
import models.MimeType;
import util.ExceptionUtil;
import util.IOUtil;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * User: Ling Hung
 * Comp: MDChiShenMe, Inc.
 * Proj: Server
 * Date: 12/8/14
 * Time: 10:10 PM
 */
public class S3BlobService implements IBlobService{
    public S3BlobService() { }

    @Override
    public Blob get(String bucket, String blobKey) {
        S3Object s3Object = getClient().getObject(bucket, blobKey);
        if (s3Object == null) {
            return null;
        }

        byte [] objectData = IOUtil.streamToBytes(s3Object.getObjectContent());

        Blob blob = new Blob();
        blob.mimeType = MimeType.fromString(s3Object.getObjectMetadata().getContentType());
        blob.data = objectData;
        blob.bucket = bucket;
        blob.key = blobKey;

        return blob;
    }

    @Override
    public String getBlobUrl(String bucket, String blobKey) {
        String blobSourceDomain = bucket + ".s3.amazonaws.com";
//        if (bucket.equals(IBlobService.BUCKET_AUDIO_PROCESSED)) {
//            blobSourceDomain = Global.getCdnAudioDomain();
//        } else if (bucket.equals(IBlobService.BUCKET_IMAGE)) {
//            blobSourceDomain = Global.getCdnImageDomain();
//        }

        //Encode the URL, per this entry on StackOverflow:
        //  http://stackoverflow.com/questions/724043/http-url-address-encoding-in-java
        try {
            URI uri = new URI(
                    "http",
                    blobSourceDomain,
                    "/" + blobKey,
                    null);
            String blobUrl = uri.toString();
            //Logger.info("Fetching blob from: " + blobUrl);
            return blobUrl;
        } catch (Exception e) {
            throw ExceptionUtil.rethrowError(e);
        }
    }

    @Override
    public String put(String bucket, String filename, byte[] blobData, MimeType mimeType) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(mimeType.value());

        if (filename.contains(".")) {
            filename = filename.substring(0, filename.lastIndexOf("."));
        }
        String blobKey = filename + "-" + UUID.randomUUID().toString() + "." + mimeType.extension();
        //We make all objects that we place in the blob store public by default
        PutObjectRequest request = new PutObjectRequest(bucket, blobKey, new ByteArrayInputStream(blobData), metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead);
        getClient().putObject(request);
        return blobKey;
    }

    @Override
    public void updateMimeType(String bucket, String blobKey, MimeType mimeType) {
        AmazonS3Client client = getClient();
        S3Object object = client.getObject(bucket, blobKey);

        if (object == null) {
            return;
        }
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(mimeType.value());

        //We make all objects that we place in the blob store public by default
        PutObjectRequest request = new PutObjectRequest(bucket, blobKey, object.getObjectContent(), metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead);
        client.putObject(request);
    }

    public void deleteBlob(String bucket, String blobKey) {
        getClient().deleteObject(bucket, blobKey);
    }

    public List<String> list(String bucket) {
        ObjectListing listing = getClient().listObjects(bucket);

        List<S3ObjectSummary> objects = listing.getObjectSummaries();
        List<String> keys = new ArrayList<String>();
        for (S3ObjectSummary o : objects) {
            keys.add(o.getKey());
        }
        return keys;
    }

    private AmazonS3Client getClient () {
        BasicAWSCredentials credentials = new BasicAWSCredentials(Global.getAwsAccessKeyId(), Global.getAwsAccessKeySecret());
        // Set AWS access credentials.
        AmazonS3Client client = new AmazonS3Client(credentials);
        return client;

    }

}
