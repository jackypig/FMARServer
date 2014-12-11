package models;

/**
 * User: Ling Hung
 * Comp: MDChiShenMe, Inc.
 * Proj: Server
 * Date: 12/9/14
 * Time: 4:01 PM
 */
public enum MimeType {
    JPEG(MediaType.IMAGE, "image/jpeg", "jpg"),
    MP4(MediaType.AUDIO, new String [] {"video/mp4", "audio/mpeg4", "audio/mpeg"}, "mp4"),
    M4A(MediaType.AUDIO, new String [] {"audio/mp4", "audio/x-m4a"}, "m4a"),
    MP3(MediaType.AUDIO, new String [] {"audio/mp3", "audio/mpeg3", "audio/mpeg"}, "mp3"),
    PNG(MediaType.IMAGE, "image/png", "png"),
    WAV(MediaType.AUDIO, new String [] {"audio/wav", "audio/x-wav"}, "wav"),
    ZIP(null, "application/zip", "zip");

    private String [] values;
    private String extension;
    private MediaType mediaType;


    MimeType(MediaType mediaType, String value, String extension) {
        this(mediaType, new String[] {value}, extension);
    }

    MimeType(MediaType mediaType, String [] values, String extension) {
        this.mediaType = mediaType;
        this.values = values;
        this.extension = extension;
    }

    public String value () {
        return this.values[0];
    }

    public String extension () {
        return extension;
    }

    public MediaType mediaType () {
        return mediaType;
    }

    public boolean isAudio () {
        return mediaType == MediaType.AUDIO;
    }

    public boolean isImage () {
        return mediaType == MediaType.IMAGE;
    }

    public static MimeType fromFilename (String filename) {
        for (MimeType mimeType : MimeType.values()) {
            if (filename.toLowerCase().endsWith(mimeType.extension)) {
                return mimeType;
            }
        }

        return null;
    }

    public static MimeType fromString (String mimeType) {
        for (MimeType mt : MimeType.values()) {
            for (String v : mt.values) {
                if (v.equals(mimeType.toLowerCase())) {
                    return mt;
                }
            }
        }

        return null;
    }
}
