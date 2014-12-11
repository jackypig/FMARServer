package services;

import models.Blob;
import models.MimeType;

/**
 * User: Ling Hung
 * Comp: MDChiShenMe, Inc.
 * Proj: Server
 * Date: 12/8/14
 * Time: 10:10 PM
 */
public interface IBlobService {
    public static final String BUCKET_IMAGE = "ms-image";

    public Blob get(String bucket, String blobKey);
    public String getBlobUrl(String bucket, String blobKey);
    public String put(String bucket, String name, byte [] blobData, MimeType mimeType);
    public void updateMimeType(String bucket, String blobKey, MimeType mimeType);
}
