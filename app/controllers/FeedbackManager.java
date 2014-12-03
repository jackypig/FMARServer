package controllers;

import play.mvc.Result;
import views.html.feedback;

/**
 * User: Ling Hung
 * Comp: XappMedia, Inc.
 * Proj: xapp-server
 * Date: 12/2/14
 * Time: 10:41 AM
 */
public class FeedbackManager extends FmarController {
    public static Result feedbackPage() {
        return ok(feedback.render());
    }

    public static Result submit() {
        return ok(feedback.render());
    }
}
