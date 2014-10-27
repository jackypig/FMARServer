package core;

import play.GlobalSettings;
import services.AwsEmailService;
import services.IEmailService;

/**
 * User: Ling Hung
 * Comp: FetchMeARestaurant, Inc.
 * Proj: fmar-server
 * Date: 10/26/14
 * Time: 4:37 PM
 */
public class Global extends GlobalSettings{
    public static Configuration configuration;
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
}
