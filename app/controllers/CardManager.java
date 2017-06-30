package controllers;

import core.Global;
import models.*;
import play.Logger;
import play.mvc.Http;
import play.mvc.Result;
import rest.RestReply;
import services.IBlobService;
import util.IOUtil;
import views.html.newCard;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Ling on 6/28/17.
 */
public class CardManager extends FmarController {
    public static Result newCard() {
        Card card = new Card();
        return ok(newCard.render(card));
    }

    public static Result editCard(Long id) {
        Card card = Card.findById(id);
        return ok(newCard.render(card));
    }

    public static Result saveCard() {
        Http.MultipartFormData formData = request().body().asMultipartFormData();
        Long cardId = null;
        if (!parameter(formData.asFormUrlEncoded(), "cardId").equals("")) {
            cardId = new Long(parameter(formData.asFormUrlEncoded(), "cardId"));
        }
        Card card;
        boolean newCard = true;
        if (cardId != null) {
            card = Card.findById(cardId);
            newCard = false;
        } else {
            card = new Card();
        }

        card.bank = Bank.valueOf( parameter(formData.asFormUrlEncoded(), "bank") );
        card.createdTimestamp = new Date();
        card.type = CardType.CREDIT;
        card.save();

        Reward reward = new Reward();
        reward.category = RewardCategory.valueOf( parameter(formData.asFormUrlEncoded(), "category") );
        reward.createdTimestamp = new Date();
        reward.type = RewardType.valueOf( parameter(formData.asFormUrlEncoded(), "type") );
        reward.amount = Float.valueOf( parameter(formData.asFormUrlEncoded(), "amount") );
        reward.card = card;
        reward.save();

        List<Card> cards = Card.findAll();
        return ok(views.html.cards.render(cards));
    }

    private static Image handleImage(Http.MultipartFormData formData, Image restaurantImage) {
        Logger.debug("Files received: " + formData.getFiles().size());
        Image image = null;

        if (formData.getFiles().size() != 0) {
            if (restaurantImage != null) {
                Global.getBlobService().deleteBlob(IBlobService.BUCKET_IMAGE, restaurantImage.blobKey);
            }
            Http.MultipartFormData.FilePart filePart = formData.getFiles().get(0);
            Logger.debug("Name: " + filePart.getFilename());
            MimeType mimeType = MimeType.fromString(filePart.getContentType());
            Logger.debug("mimeType: " + mimeType);
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
        } else if (restaurantImage != null){
            image = restaurantImage;
        }

        return image;
    }

    public static Result restaurantList() {
        List<Restaurant> restaurants = Restaurant.findAll();
        Collections.sort(restaurants);
        return ok(views.html.restaurants.render(restaurants));
    }
}
