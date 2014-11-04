package controllers;

import core.Global;
import models.LoginType;
import models.Restaurant;
import models.User;
import play.*;
import play.api.mvc.*;
import play.libs.Json;
import play.mvc.*;

import play.mvc.Result;
import play.mvc.Results;
import rest.RestReply;
import views.html.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Application extends FmarController {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public static Result loginPage () {
        return ok(login.render());
    }

    public static Result register () {
        return ok(register.render());
    }

    public static Result admin() {
        return ok(admin.render());
    }

    public static Result newRestaurant() {
        Restaurant restaurant = new Restaurant();
        return ok(newRestaurant.render(restaurant));
    }

    public static Result editRestaurant(Long id) {
        Restaurant restaurant = Restaurant.findById(id);
        return ok(newRestaurant.render(restaurant));
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

    public static Result saveRestaurant() {
        Long restaurantId = formValueAsLong("restaurantId");
        Restaurant restaurant;
        if (restaurantId != null) {
            restaurant = Restaurant.findById(restaurantId);
        } else {
            restaurant = new Restaurant();
        }

        restaurant.englishName = formValue("englishName");
        restaurant.foreignName = formValue("foreignName");
        restaurant.category = formValue("category");
        restaurant.state = formValue("state");
        restaurant.city = formValue("city");
        restaurant.address = formValue("address");
        restaurant.telephone = formValue("telephone");
        restaurant.createdTimestamp = new Date();
        restaurant.save();

        List<Restaurant> restaurants = Restaurant.findAll();
        Collections.sort(restaurants);
        sendNotifications(restaurants, 2);

        return ok(views.html.restaurants.render(restaurants));
    }

    public static Result restaurantList() {
        List<Restaurant> restaurants = Restaurant.findAll();
        Collections.sort(restaurants);
        return ok(views.html.restaurants.render(restaurants));
    }

    public static void sendNotifications (List<Restaurant> restaurants, int numberOfRestaurantToShow) {
        Collections.shuffle(restaurants);
        String email = Global.getSystemEmail();
        String fromEmail = Global.getSystemEmail();
        String subject = "New restaurants are here";
        String body = "The following are the details of the new opened restaurants: <br><br>";

        for (int i=0; i<numberOfRestaurantToShow; i++) {
            body += "English Name: " + restaurants.get(i).englishName + "<br>" +
                    "Foreign Name: " + restaurants.get(i).foreignName + "<br>" +
                    "Category: " + restaurants.get(i).category + "<br>" +
                    "State: " + restaurants.get(i).state + "<br>" +
                    "City: " + restaurants.get(i).city + "<br>" +
                    "Address: " + restaurants.get(i).address + "<br>" +
                    "Phone Number: " + restaurants.get(i).telephone + "<br><br>";
        }

        body += "Thanks,<br>" +
                "The FetchMeARestaurant Team";
        Global.getEmailService().send(fromEmail, "FetchMeARestaurant Admin", new String[] {email}, subject, body, body);
    }

    public static Result deleteRestaurant(Long Id) {
        Restaurant restaurant = Restaurant.findById(Id);
        restaurant.delete();
//        return ok(RestController.success().toJson());
        return ok(Json.toJson(true));
    }


}
