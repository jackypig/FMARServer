package core;

import play.GlobalSettings;
import services.*;
import util.IOUtil;

/**
 * User: Ling Hung
 * Comp: MDChiShenMe, Inc.
 * Proj: Server
 * Date: 10/26/14
 * Time: 4:37 PM
 */
public class Global extends GlobalSettings{
    public static Configuration configuration;
    public static String applicationVersion ;
    public static String getAwsAccessKeyId () {
//        return configuration.getAwsAccessKeyId();
        return "AKIAJQS5CTQCDJ6Y4UXQ";
    }

    public static String getAwsAccessKeySecret () {
//        return configuration.getAwsAccessKeySecret();
        return "FbOYOn4tis645mc6Dx7srOWvLx8bzNSKKR7sJH0R";
    }

    public static IEmailService getEmailService() {
//        return configuration.getEmailService();
        return new AwsEmailService();
    }

    public static String getSystemEmail () {
        return "jackypig0906@gmail.com";
    }

    public static IBlobService getBlobService() {
//        return configuration.getBlobService();
        return new S3BlobService();
    }

    public static String getCdnAudioDomain () {
        return play.Configuration.root().getString("cdn.audio.domain");
    }

    public static String getCdnImageDomain () {
        return play.Configuration.root().getString("cdn.image.domain");
    }

    public static boolean isDebugEnabled () {
        return false;
    }

    public static AuthenticateService getAuthenticationService () {
        return new AuthenticateService();
    }

    public static String getApplicationVersion() {
        String version = "";
        if (applicationVersion != null) {
            version = applicationVersion;
        } else {
            if (!IOUtil.resourceExists("version.sbt")) {
                return "dev-no-file";
            }
            String versionRaw = IOUtil.resourceToString("version.sbt");
            String[] pieces = versionRaw.split("\"");
            if (pieces.length < 2) {
                return "dev-wrong-format";
            }
            version = pieces[1];
        }

        return version.trim();
    }
}
