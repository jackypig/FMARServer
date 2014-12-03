package core;

import play.GlobalSettings;
import services.AuthenticateService;
import services.AwsEmailService;
import services.IEmailService;
import util.IOUtil;

/**
 * User: Ling Hung
 * Comp: FetchMeARestaurant, Inc.
 * Proj: fmar-server
 * Date: 10/26/14
 * Time: 4:37 PM
 */
public class Global extends GlobalSettings{
    public static Configuration configuration;
    public static String applicationVersion ;
    public static String getAwsAccessKeyId () {
//        return configuration.getAwsAccessKeyId();
        return "AKIAJWZ6PVW5UWXQF3WA";
    }

    public static String getAwsAccessKeySecret () {
//        return configuration.getAwsAccessKeySecret();
        return "ifdURk4NsApbZ30q6oNb9mzU56UfjoCVhPvJDzDu";
    }

    public static IEmailService getEmailService() {
//        return configuration.getEmailService();
        return new AwsEmailService();
    }

    public static String getSystemEmail () {
        return "jackypig0906@gmail.com";
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
