package controllers;

import com.avaje.ebean.Ebean;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.*;
import com.google.gdata.util.ServiceException;
import core.Global;
import models.*;
import play.Logger;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import rest.RestReply;
import services.IBlobService;
import util.IOUtil;
import views.html.helper.form;
import views.html.newRestaurant;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * User: Ling Hung
 * Comp: MDChiShenMe, Inc.
 * Proj: Server
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

    public static Result toggleApproval(Long id) {
        Restaurant restaurant = Restaurant.findById(id);
        if (restaurant.approved) {
            restaurant.approved = false;
        } else {
            restaurant.approved = true;
        }

        restaurant.save();
        return RestController.ok(new RestReply(true));
    }

    public static Result saveRestaurant() {
        Http.MultipartFormData formData = request().body().asMultipartFormData();
        Long restaurantId = new Long(parameter(formData.asFormUrlEncoded(), "restaurantId"));
        Restaurant restaurant;
        boolean newRestaurant = true;
        if (restaurantId != null) {
            restaurant = Restaurant.findById(restaurantId);
            newRestaurant = false;
        } else {
            restaurant = new Restaurant();
        }

        restaurant.address = parameter(formData.asFormUrlEncoded(), "address");
        restaurant.approved = false;
        restaurant.category = parameter(formData.asFormUrlEncoded(), "category");
        restaurant.city = parameter(formData.asFormUrlEncoded(), "city");
        restaurant.createdBy = SessionManager.getUser();
        restaurant.createdTimestamp = new Date();
        restaurant.englishName = parameter(formData.asFormUrlEncoded(), "englishName");
        restaurant.foreignName = parameter(formData.asFormUrlEncoded(), "foreignName");
        restaurant.image = saveImage(formData, restaurant);
        restaurant.specialOffer = parameter(formData.asFormUrlEncoded(), "specialOffer");
        restaurant.state = parameter(formData.asFormUrlEncoded(), "state");
        restaurant.telephone = parameter(formData.asFormUrlEncoded(), "telephone");
        restaurant.save();

        List<Restaurant> restaurants = Restaurant.findAll();
        Collections.sort(restaurants);
        //TODO - Think about how to send email when restaurants are updated
//        sendNotifications(restaurants, 4);

        if (newRestaurant) {
            sendNotifications(restaurant, "A new restaurant has been created");
        } else {
            sendNotifications(restaurant, "A restaurant has been modified");
        }

        return ok(views.html.restaurants.render(restaurants));
    }

    private static Image saveImage(Http.MultipartFormData formData, Restaurant restaurant) {
        Logger.debug("Files received: " + formData.getFiles().size());
        Http.MultipartFormData.FilePart filePart = formData.getFiles().get(0);
        Logger.debug("Name: " + filePart.getFilename());
        MimeType mimeType = MimeType.fromString(filePart.getContentType());
        Logger.debug("mimeType: " + mimeType);

        Image image = null;

        if (formData.getFiles().size() != 0) {
            image = new Image();
            image.createdTimestamp = new Date();
            image.fileLength = filePart.getFile().length();
//        image.filename = Media.createUniqueName(restaurant.id, mimeType.imageType(), filePart.getFilename());
            image.filename = filePart.getFilename();
            image.mimeType = mimeType;
            image.uploadedBy = SessionManager.getUser();
            byte [] fileBytes = IOUtil.fileToBytes(filePart.getFile());
            String blobKey = null;
            Logger.debug("FileBytes: " + fileBytes.length);
            blobKey = Global.getBlobService().put(IBlobService.BUCKET_IMAGE, image.filename, fileBytes, mimeType);
            image.blobKey = blobKey;
            image.status = MediaStatus.READY;
            image.save();
        }

        return image;
    }

    public static Result restaurantList() {
        List<Restaurant> restaurants = Restaurant.findAll();
        Collections.sort(restaurants);
        return ok(views.html.restaurants.render(restaurants));
    }

//    public static void sendNotifications (List<Restaurant> restaurants, int numberOfRestaurantToShow) {
//        Collections.shuffle(restaurants);
//        String email = Global.getSystemEmail();
//        String fromEmail = Global.getSystemEmail();
//        String subject = "New restaurants are here";
//        String body = "The following are the details of the new opened restaurants: <br><br>";
//
//        for (int i=0; i<numberOfRestaurantToShow; i++) {
//            body += "English Name: " + restaurants.get(i).englishName + "<br>" +
//                    "Foreign Name: " + restaurants.get(i).foreignName + "<br>" +
//                    "Category: " + restaurants.get(i).category + "<br>" +
//                    "State: " + restaurants.get(i).state + "<br>" +
//                    "City: " + restaurants.get(i).city + "<br>" +
//                    "Address: " + restaurants.get(i).address + "<br>" +
//                    "Phone Number: " + restaurants.get(i).telephone + "<br><br>";
//        }
//
//        body += "Thanks,<br>" +
//                "The FetchMeARestaurant Team";
//        Global.getEmailService().send(fromEmail, "FetchMeARestaurant Admin", new String[] {email}, subject, body, body);
//    }

    public static void sendNotifications (Restaurant restaurant, String emailSubject) {
        String email = Global.getSystemEmail();
        String fromEmail = Global.getSystemEmail();
        String subject = emailSubject;
        String body = "The following are the details of the restaurant: <br><br>";
        String imageField;
        if (restaurant.image != null) {
            imageField = "<img src=" + restaurant.image.imageUrl() + ">";
        } else {
            imageField = "No image uploaded";
        }

        body += "English Name: " + restaurant.englishName + "<br>" +
                "Foreign Name: " + restaurant.foreignName + "<br>" +
                "Image: " + imageField + "<br>" +
                "Category: " + restaurant.category + "<br>" +
                "State: " + restaurant.state + "<br>" +
                "City: " + restaurant.city + "<br>" +
                "Address: " + restaurant.address + "<br>" +
                "Phone Number: " + restaurant.telephone + "<br><br>" +
                "Thanks,<br>" +
                "The MDChiShenMe Team";
        Global.getEmailService().send(fromEmail, "MDChiShenMe Admin", new String[] {email}, subject, body, body);
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
