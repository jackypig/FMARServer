package util;

import play.Play;
import play.libs.WS;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

/**
 * User: Ling Hung
 * Comp: XappMedia, Inc.
 * Proj: xapp-server
 * Date: 11/3/14
 * Time: 10:58 PM
 */
public class IOUtil {
    private static Logger logger = Logger.getLogger(IOUtil.class.getName());
    private static int BUFFER_LENGTH = 2000;

    public static void write (InputStream source, OutputStream target) {
        byte [] buffer = new byte[1024];
        int bytes = 0;
        try {
            while ((bytes = source.read(buffer)) != -1) {
                target.write(buffer, 0, bytes);
                target.flush();
            }
        } catch (IOException e) {
            throw ExceptionUtil.rethrowError(e);
        }
    }

    public static boolean resourceExists (String resource) {
        return Thread.currentThread().getContextClassLoader().getResource(resource) != null;
    }

    public static boolean playResourceExists (String resource) {
        return Play.application().resourceAsStream(resource) != null;
    }

    public static String playResourceToString (String resource) {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
        byte[] bytes = streamToBytes(is);
        return byteArrayToString(bytes);
    }

    public static byte [] resourceToBytes (String resource) {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
        return streamToBytes(is);
    }

    public static String resourceToString (String resource) {
        byte [] bytes = resourceToBytes(resource);
        return byteArrayToString(bytes);
    }

    public static String byteArrayToString(byte[] bytes) {
        try {
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw ExceptionUtil.rethrowError(e);
        }
    }

    public static InputStream resourceToStream (String resource) {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
        return is;
    }

    public static void bytesToFile(byte [] data, File file) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            write(bais, fos);
        } catch (Exception e) {
            throw ExceptionUtil.rethrowError(e);
        }
    }

    public static byte [] fileToBytes (File file) {
        FileInputStream is = null;
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw ExceptionUtil.rethrowError(e);
        }
        return streamToBytes(is);
    }

    public static String fileToString (File file) {
        FileInputStream is = null;
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw ExceptionUtil.rethrowError(e);
        }
        return streamToString(is);
    }

    public static byte [] streamToBytes (InputStream stream) {
        ByteBuffer buffer = streamToByteBuffer(stream);
        byte [] bytes = new byte[buffer.position()];
        buffer.flip();
        buffer.get(bytes);
        return bytes;
    }

    public static ByteBuffer streamToByteBuffer (InputStream stream) {
        BufferedInputStream is = new BufferedInputStream(stream);
        byte [] bytes = new byte[BUFFER_LENGTH];
        int bytesRead = 0;

        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_LENGTH*2);
        while ((bytesRead = read(is, bytes)) != -1) {
            if (buffer.position() + bytesRead > buffer.capacity()) {
                ByteBuffer oldBuffer = buffer;
                buffer = ByteBuffer.allocate(buffer.capacity()*2);
                buffer.put(oldBuffer.array(), 0, oldBuffer.position());
            }
            buffer.put(bytes, 0, bytesRead);
        }
        return buffer;
    }

    public static File downloadUrl (String url) {
        url = url.replace(" ", "+");
        play.Logger.info("URLToDownload: " + url);
        WS.WSRequestHolder requestHolder = WS.url(url);
        WS.Response response = requestHolder.get().get();
        byte [] inputData = response.asByteArray();

        String fileExtension = url.substring(url.length() - 4);
        File tempFile = null;
        try {
            tempFile = File.createTempFile("s3in", fileExtension);
            tempFile.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(tempFile);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bos.write(inputData);
            bos.flush();
            bos.close();
            return tempFile;
        } catch (IOException e) {
            e.printStackTrace();
            throw ExceptionUtil.rethrowError(e);
        }
    }

    public static String streamToString (InputStream stream) {
        byte [] bytes = streamToBytes(stream);
        return new String(bytes);
    }

    private static int read(InputStream is, byte [] bytes) {
        try {
            int bytesRead = is.read(bytes);
            return bytesRead;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
