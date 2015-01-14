package controllers;

import core.Global;
import models.LoginType;
import models.User;
import play.Logger;
import play.libs.Json;
import play.mvc.Result;
import rest.RestReply;
import util.EmailHelper;

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
        User user = User.findByEmail(email);

        RegistrationReply reply = new RegistrationReply();
        if (user != null) {
            reply.success = false;
        } else {
            reply.success = true;
            saveGuestUser(email);
            EmailHelper.sendWelcomeEmail(email);
            EmailHelper.sendSpecialOfferEmail(email);
            EmailHelper.notifyAdmin(email);
        }
        return ok(Json.toJson(reply));
    }

    public static void saveGuestUser(String email) {
        User user = new User();
        user.email = email;
        user.loginType = LoginType.GUEST;
        user.active = true;
        user.save();
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
