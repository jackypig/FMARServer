package controllers;

import com.avaje.ebean.Ebean;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.*;
import com.google.gdata.util.ServiceException;
import core.Global;
import models.Restaurant;
import play.Logger;
import play.libs.Json;
import play.mvc.Result;
import views.html.newRestaurant;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * User: Ling Hung
 * Comp: XappMedia, Inc.
 * Proj: xapp-server
 * Date: 11/6/14
 * Time: 8:52 AM
 */
public class RestaurantManager extends FmarController {
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
//        sendNotifications(restaurants, 4);

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

    public static Result syncFromGoogleSpreadsheet() {
        try {
            testSpreadsheet();
        } catch (Exception e){
            e.printStackTrace();
        }
        return ok(Json.toJson(true));
    }

    public static void testSpreadsheet() throws MalformedURLException, IOException, ServiceException {
        SpreadsheetService service =
                new SpreadsheetService("MySpreadsheetIntegration-v1");

        // TODO: Authorize the service object for a specific user (see other sections)

        service.setUserCredentials("jackypig0906@gmail.com", "Arnold0906");

        // Define the URL to request.  This should never change.
        URL SPREADSHEET_FEED_URL = new URL(
                "https://spreadsheets.google.com/feeds/spreadsheets/private/full");

        // Make a request to the API and get all spreadsheets.
        SpreadsheetFeed feed = service.getFeed(SPREADSHEET_FEED_URL,
                SpreadsheetFeed.class);
        List<SpreadsheetEntry> spreadsheets = feed.getEntries();

        if (spreadsheets.size() == 0) {
            // TODO: There were no spreadsheets, act accordingly.
        }

        SpreadsheetEntry spreadsheet = spreadsheets.get(0);
        System.out.println("Spreadsheet name:" + spreadsheet.getTitle().getPlainText());

        WorksheetFeed worksheetFeed = service.getFeed(
                spreadsheet.getWorksheetFeedUrl(), WorksheetFeed.class);
        List<WorksheetEntry> worksheets = worksheetFeed.getEntries();

        for (WorksheetEntry worksheet: worksheets) {
            System.out.println("State: " + worksheet.getTitle().getPlainText());

            // Fetch the list feed of the worksheet.
            URL listFeedUrl = worksheet.getListFeedUrl();
            ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);

            // Iterate through each row, printing its cell values.
            for (ListEntry row : listFeed.getEntries()) {
                saveToDB(worksheet.getTitle().getPlainText(), row);
            }
        }
    }

    public static void saveToDB (String state, ListEntry row) {
        String englishName = row.getCustomElements().getValue("englishname");
        Logger.info("Copying restaurant: " + row.getCustomElements().getValue("chinesename"));

        Restaurant restaurant = Restaurant.findByName(englishName);
        if (restaurant == null) {
            restaurant = new Restaurant();
        }

        restaurant.englishName = englishName;
        restaurant.foreignName = row.getCustomElements().getValue("chinesename");
        restaurant.category = "Chinese";
        restaurant.state = state;
        restaurant.city = row.getCustomElements().getValue("city");
        restaurant.address = row.getCustomElements().getValue("address");
        restaurant.telephone = row.getCustomElements().getValue("phonenumber");
        restaurant.createdTimestamp = new Date();
        Ebean.beginTransaction();
        Ebean.save(restaurant);
        Ebean.commitTransaction();
        Ebean.endTransaction();
    }
}
