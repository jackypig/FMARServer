package controllers;

import core.Global;
import models.Restaurant;
import play.*;
import play.mvc.*;

import views.html.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Application extends FmarController {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public static Result admin() {
        return ok(admin.render());
    }

    public static Result newRestaurant() {
        return ok(newRestaurant.render());
    }

    public static Result saveRestaurant() {
        Long restaurantId = formValueAsLong("restaurantId");
        Restaurant restaurant;
        if (restaurantId != null) {
            restaurant = Restaurant.findById(restaurantId);
        } else {
            restaurant = new Restaurant();
        }

        restaurant.name = formValue("name");
        restaurant.location = formValue("location");
        restaurant.telephone = formValue("telephone");
        restaurant.createdTimestamp = new Date();
        restaurant.introduction = formValue("introduction");
        restaurant.save();

        sendNotifications(restaurant);
        List<Restaurant> restaurants = Restaurant.findAll();
        Collections.sort(restaurants);

        return ok(views.html.restaurants.render(restaurants));
    }

    public static Result restaurantList() {
        List<Restaurant> restaurants = Restaurant.findAll();
        Collections.sort(restaurants);
        return ok(views.html.restaurants.render(restaurants));
    }

    public static void sendNotifications (Restaurant restaurant) {
        String email = Global.getSystemEmail();
        String fromEmail = Global.getSystemEmail();
        String subject = "A new restaurant named" + restaurant.name + "has been created";
        String body = "The following are the details of this restaurant: <br><br>" +
                "Name: " + restaurant.name + "<br>" +
                "Location: " + restaurant.location + "<br>" +
                "Phone Number: " + restaurant.telephone + "<br><br>" +
                "Thanks,<br>" +
                "The FetchMeARestaurant Team";
        Global.getEmailService().send(fromEmail, "FetchMeARestaurant Admin", new String[] {email}, subject, body, body);
    }
}
