package controllers;

import core.Global;
import models.LoginType;
import models.User;
import play.Logger;
import play.libs.Json;
import play.mvc.Result;
import rest.RestReply;

import java.util.List;

/**
 * User: Ling Hung
 * Comp: MDChiShenMe, Inc.
 * Proj: server
 * Date: 10/29/14
 * Time: 10:56 PM
 */
public class UserManager extends FmarController{
    public static Result list() {
        List<User> users = User.findAll();
        return ok(views.html.users.render(users));
    }

    public static Result delete(Long Id) {
        User user = User.findById(Id);
        user.delete();
        return ok(Json.toJson(true));
    }

    public static Result signInAuthenticate (String email, String password) {
        Logger.trace("Authenticating: " + email);
        boolean valid = Global.getAuthenticationService().authenticate(email, password);
        if (!valid) {
            return ok(RestController.error("INVALID_LOGIN", "Invalid email or password").toJson());
        }

        User user = User.findByEmail(email);

        RegistrationReply reply = new RegistrationReply();
        if (user != null) {
            reply.success = true;
            SessionManager.create(user);
        } else {
            reply.success = false;
        }
//        Object payload = new RestReply(true) {
//            public String loginType = user.loginType.toString();
//        };
//        return ok(Json.toJson(payload));
        return ok(Json.toJson(reply));
    }

    public static Result stayUpdatedAuthenticate (String email) {
        Logger.trace("Authenticating: " + email);
//        boolean valid = Global.getAuthenticationService().authenticate(email, password);
//        if (!valid) {
//            return ok(RestController.error("INVALID_LOGIN", "Invalid email or password").toJson());
//        }

        User user = User.findByEmail(email);

        RegistrationReply reply = new RegistrationReply();
        if (user != null) {
            reply.success = false;
            sendWelcomeEmail(email);
        } else {
            reply.success = true;
            saveRegularUser(email);
            sendWelcomeEmail(email);
        }
//        SessionManager.create(user);
//        Object payload = new RestReply(true) {
//            public String loginType = user.loginType.toString();
//        };
//        return ok(Json.toJson(payload));
        return ok(Json.toJson(reply));
    }

    public static void saveRegularUser(String email) {
        User user = new User();
        user.email = email;
        user.loginType = LoginType.GUEST;
        user.save();
    }

    public static void sendWelcomeEmail(String email) {
        String fromEmail = Global.getSystemEmail();
        String subject = "New restaurants are here";
        String htmlBody = getWelcomeEmailHtmlBody();
        String plainTextBody = getWelcomeEmailPlainTextBody();
        Global.getEmailService().send(fromEmail, "FetchMeARestaurant Admin", new String[] {email}, subject, htmlBody, plainTextBody);
    }

    public static String getWelcomeEmailHtmlBody() {
        String htmlBody = "<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>" +
                            "<html xmlns='http://www.w3.org/1999/xhtml'>" +
                            "<head>" +
                            "<meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />" +
                            "<title>Demystifying Email Design</title>" +
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
                                                        "you can find and stay updated on the most recent special offers from the restaurants in DC area." +
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

    public static String getWelcomeEmailPlainTextBody() {
        String plainTextBody = "Test Body";
        return plainTextBody;
    }

    public static Result saveUser() {
        User user = new User();
        user.firstName = parameter(request().body().asFormUrlEncoded(), "firstName");
        user.lastName = parameter(request().body().asFormUrlEncoded(), "lastName");
        user.email = parameter(request().body().asFormUrlEncoded(), "email");
        user.passwordEncrypted = Global.getAuthenticationService().encrypt(parameter(request().body().asFormUrlEncoded(), "password"));
        user.active = true;
        user.loginType = LoginType.fromString(parameter(request().body().asFormUrlEncoded(), "loginType"));
        user.save();

        RegistrationReply reply = new RegistrationReply();
        reply.success = true;
        reply.nextUrl = "/restaurantList";

//        if (!SessionManager.isLoggedIn()) {
//            SessionManager.create(user);
//        }

        return ok(Json.toJson(reply));
    }

    public static class RegistrationReply extends RestReply {
        public String nextUrl;
    }
}
