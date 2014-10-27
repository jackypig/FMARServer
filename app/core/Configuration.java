package core;

import services.AwsEmailService;
import services.IEmailService;

/**
 * User: Ling Hung
 * Comp: FetchMeARestaurant, Inc.
 * Proj: fmar-server
 * Date: 10/26/14
 * Time: 4:51 PM
 */
public class Configuration {
    public String getServerHost () {
        return "localhost";
    }

    public int getServerPort () {
        return 9000;
    }

    public IEmailService getEmailService () {
        return new AwsEmailService();
    }

    public String getAwsAccessKeyId () {
        return "AKIAJQS5CTQCDJ6Y4UXQ";
    }

    public String getAwsAccessKeySecret () {
        return "FbOYOn4tis645mc6Dx7srOWvLx8bzNSKKR7sJH0R";
    }
}
