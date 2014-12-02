package controllers;

import play.mvc.Result;
import views.html.*;

public class Application extends FmarController {

    public static Result homePage () {
        return ok(home.render());
    }

    public static Result register () {
        return ok(register.render());
    }

    public static Result admin() {
        return ok(admin.render());
    }
}
