package controllers;

import core.Global;
import models.Restaurant;
import play.*;
import play.api.mvc.*;
import play.libs.Json;
import play.mvc.*;

import play.mvc.Result;
import play.mvc.Results;
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

    public static List<Restaurant> pickRestaurants(int numberOfRestaurantToShow) {
        List<Restaurant> restaurants = Restaurant.findAll();
        Random random = new Random();
        return restaurants;
    }
}
