package controllers;

import core.Global;
import models.LoginType;
import models.User;
import play.Logger;
import play.libs.Json;
import play.mvc.Result;
import rest.RestReply;

/**
 * User: Ling Hung
 * Comp: XappMedia, Inc.
 * Proj: xapp-server
 * Date: 10/29/14
 * Time: 10:56 PM
 */
public class UserManager extends FmarController{
    public static Result authenticate (String email, String password) {
        Logger.trace("Authenticating: " + email);
        boolean valid = Global.getAuthenticationService().authenticate(email, password);
        if (!valid) {
            return ok(RestController.error("INVALID_LOGIN", "Invalid email or password").toJson());
        }

        final User user = User.findByEmail(email);
        SessionManager.create(user);
//        if (user.adminType == AdminType.ADVERTISER) {
//            SessionManager.setAdvertiser(user.advertiser);
//        } else if (user.isSuperuser() || user.isXappUser()) {
//            //If there is a publisher associated with the super user, use that
//            //Otherwise just take the first one
//            if (admin.publisher != null) {
//                SessionManager.setPublisher(admin.publisher);
//            } else {
//                SessionManager.setPublisher(Publisher.findById(1L));
//            }
//        } else if (admin.adminType == AdminType.PUBLISHER) {
//            SessionManager.setPublisher(admin.publisher);
//        }

        Object payload = new RestReply(true) {
            public String loginType = user.loginType.toString();
        };
        return ok(Json.toJson(payload));
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
