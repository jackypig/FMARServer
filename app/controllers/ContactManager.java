package controllers;

import play.mvc.Result;
import views.html.contact;

/**
 * User: Ling Hung
 * Comp: XappMedia, Inc.
 * Proj: xapp-server
 * Date: 12/2/14
 * Time: 10:41 AM
 */
public class ContactManager extends FmarController {
    public static Result contactPage() {
        return ok(contact.render());
    }

    public static Result submit() {
        return ok(contact.render());
    }
}
