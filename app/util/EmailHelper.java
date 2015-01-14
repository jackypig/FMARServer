package util;

import core.Global;
import models.Image;
import models.Restaurant;
import models.User;

import java.util.List;

/**
 * Created by jpk on 1/13/15.
 */
public class EmailHelper {
    private static final String SERVER = Global.getApplicationUrl();

    public static void notifyAdmin (String guestEmail) {
        String email = Global.getSystemEmail();
        String fromEmail = Global.getSystemEmail();
        String subject = "A new user has arrived";
        String body = "The following are the details of the new guest user: <br><br>";

        body += "User Email: " + guestEmail + "<br>" +
                "Thanks,<br>" +
                "The MDChiShenMe Team";
        Global.getEmailService().send(fromEmail, "MDChiShenMe Admin", new String[] {email}, subject, body, body);
    }

    public static void sendWelcomeEmail(String email) {
        String fromEmail = Global.getSystemEmail();
        String subject = "Welcome to MDChiShenMe";
        String htmlBody = EmailHelper.getWelcomeEmailHtmlBody();
        String plainTextBody = EmailHelper.getWelcomeEmailPlainTextBody();
        Global.getEmailService().send(fromEmail, "MDChiShenMe Admin", new String[] {email}, subject, htmlBody, plainTextBody);
    }

    public static void sendSpecialOfferEmail(String email) {
        String fromEmail = Global.getSystemEmail();
        String subject = "Special offers for this week!";
        List<Restaurant> restaurants = Restaurant.findWithSpecialOffer();
        String toAddresses;
        if (email != null) {
            toAddresses = email;
        } else  {
            toAddresses = findUserEmails();
        }

        System.out.println("Number of restaurants with special offer: " + restaurants.size());
        String htmlBody = EmailHelper.getSpecialOfferEmailBody(restaurants);
        String plainTextBody = EmailHelper.getWelcomeEmailPlainTextBody();
        Global.getEmailService().send(fromEmail, "MDChiShenMe Admin", new String[] {toAddresses}, subject, htmlBody, plainTextBody);
    }

    public static String getWelcomeEmailHtmlBody() {
        String htmlBody = "<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>" +
                "<html xmlns='http://www.w3.org/1999/xhtml'>" +
                "<head>" +
                "<meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'/>" +
                "<body style='margin: 0; padding: 0;'>" +
                "<table align='center' style='border: solid #d3d3d3 20px' cellpadding='0' cellspacing='0' width='600'>" +
                "<tr>" +
                "<td style='font-family: cursive; font-size: 50pt' align='center' width='300' height='80' bgcolor='#70bbd9'>" +
                "MDChiShenMe" +
                "</td>" +
                "</tr>" +
                "<tr>" +
                "<td bgcolor='#ffffff' style='padding: 0px 30px 0px 30px;'>" +
                "<table border='0' cellpadding='0' cellspacing='0' width='100%'>" +
                "<tr>" +
                "<td style='font-weight: bold; font-family: Calibri, Helvetica, Arial, sans-serif; font-size: 30pt; padding: 20px 0 30px 0;'>" +
                "Welcome!" +
                "</td>" +
                "</tr>" +
                "<tr>" +
                "<td style='font-family: Calibri, Helvetica, Arial, sans-serif; font-size: 14pt; padding: 0px 0 30px 0;'>" +
                "Welcome to MDChiShenMe, this is a place where collects the special offers for the restaurants in DC area. By joining this community," +
                "you can find and stay updated on the most recent special offers from the local restaurants in Maryland, Virginia, and DC." +
                "You will receive email periodically, as we receive new special offers of the local restaurants, and the news about the newly opened restaurants. You can " +
                "also share the restaurants and the special offers with us and we'll update our database immediately after verification. Thank you for joining us, welcome aboard!" +
                "</td>" +
                "</tr>" +
                "<tr>" +
                "<td style='font-family: Calibri, Helvetica, Arial, sans-serif; font-size: 14pt;padding: 30px 0px'>" +
                "MDChiShenMe Team" +
                "</td>" +
                "</tr>" +
                "</table>" +
                "</td>" +
                "</tr>" +
                "<tr>" +
                "<td bgcolor='#ee4c50' style='padding: 30px 30px 30px 30px;'>" +
                "You are receiving this email because you provided us with your email address through the MDChiShenMe website. " +
                "If you no longer wish to receive emails from us, please <a href=''>unsubscribe</a> and you will be removed from our " +
                "email list." +
                "</td>" +
                "</tr>" +
                "</table>" +
                "</body>" +
                "</head>" +
                "</html>";
        return htmlBody;
    }

    public static String getSpecialOfferEmailBody(List<Restaurant> restaurants) {
        String restaurantBody = "";

        for (Restaurant restaurant: restaurants) {
            restaurantBody += "<tr>" +
                    "<td style='text-align: center; padding-bottom: 20px'>" +
                    "<img class='img-responsive' src='" + imageUrl(restaurant.image)+ "' alt='Not available' width='300px'><br>" +
                    "Restaurant Name: " + restaurant.englishName + " (Also known as: " + restaurant.foreignName + ")<br>" +
                    "Address:" + restaurant.address + "<br>" +
                    "<font style='font-weight: bold'>Special Offer: " + restaurant.specialOffer + "</font><br>" +
                    "</td>" +
                    "</tr>";
        }

        String emailBody = "<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>" +
                "<html xmlns='http://www.w3.org/1999/xhtml'>" +
                "<head>" +
                "<meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'/>" +
                "<body style='margin: 0; padding: 0;'>" +
                "<table align='center' style='border: solid #d3d3d3 20px' cellpadding='0' cellspacing='0' width='600'>" +
                "<tr>" +
                "<td style='font-family: cursive; font-size: 50pt' align='center' width='300' height='80' bgcolor='#70bbd9'>MDChiShenMe</td>" +
                "</tr>" +
                "<tr>" +
                "<td bgcolor='#ffffff' style='padding: 0px 30px 0px 30px;'>" +
                "<table border='0' cellpadding='0' cellspacing='0' width='100%'>" +
                "<tr>" +
                "<td style='font-weight: bold; font-family: Calibri, Helvetica, Arial, sans-serif; font-size: 30pt; padding: 20px 0 30px 0;'>Hey, here are the special offers for this week!!</td>" +
                "</tr>" +
                restaurantBody +
                "<tr>" +
                "<td colspan='2' style='padding-top: 20px; text-align: center'>" +
                "<a href='" + SERVER + "' target='_blank'>Visit us to see more special offers from your local restaurants!</a>" +
                "</td>" +
                "</tr>" +
                "<tr>" +
                "<td colspan='2' style='font-family: Calibri, Helvetica, Arial, sans-serif; font-size: 15pt;padding: 30px 0px'>MDChiShenMe Team</td>" +
                "</tr>" +
                "</table>" +
                "</td>" +
                "</tr>" +
                "<tr>" +
                "<td bgcolor='#ee4c50' style='padding: 30px 30px 30px 30px;'>" +
                "You are receiving this email because you provided us with your email address through the MDChiShenMe website. If you no longer wish to receive emails from us, please <a href=''>unsubscribe</a> and you will be removed from our email list." +
                "</td>" +
                "</tr>" +
                "</table>" +
                "</body>" +
                "</head>" +
                "</html>";

        return emailBody;
    }

    public static String findUserEmails () {
        List<User> users = User.findAll();
        System.out.println("Number of emails to send: " + users.size());
        String userEmailsString =  "";
        for (int i=0; i<users.size(); i++) {
            if (i == users.size()-1) {
                userEmailsString += users.get(i).email;
            } else {
                userEmailsString += users.get(i).email + ",";
            }
        }
        return userEmailsString;
    }

    public static String getWelcomeEmailPlainTextBody() {
        String plainTextBody = "Welcome to MDChiShenMe";
        return plainTextBody;
    }

    private static String imageUrl(Image image) {
        if (image == null) {
            return null;
        } else {
            return image.imageUrl();
        }
    }
}
