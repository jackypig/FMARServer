package controllers;

import core.Global;
import models.Restaurant;
import play.libs.Json;
import play.mvc.Result;
import rest.RestReply;
import views.html.feedback;

import java.util.Collections;
import java.util.List;

/**
 * User: Ling Hung
 * Comp: MDChiShenMe, Inc.
 * Proj: Server
 * Date: 12/2/14
 * Time: 10:41 AM
 */
public class FeedbackManager extends FmarController {
    public static Result feedbackPage() {
        return ok(feedback.render());
    }

    public static Result submit() {
        String email = formValue("email");
        String name = formValue("name");
        String subject = formValue("subject");
        String comment = formValue("comment");
        sendNotification(email, name, subject, comment);

        List<Restaurant> restaurants = Restaurant.findAll();
        Collections.sort(restaurants);
        return ok(views.html.restaurants.render(restaurants));
    }

    public static void sendNotification(String toEmail, String name, String subject, String comment) {
        String fromEmail = Global.getSystemEmail();
        String body = "From " + toEmail + " " + name + " saying:" + "<br>" + comment;
        Global.getEmailService().send(fromEmail, "MDChiShenMe Admin", new String[] {fromEmail}, subject, body, body);
    }
}
