package models;

import core.Global;
import play.db.ebean.Model;
import services.IBlobService;

import javax.persistence.*;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * User: Ling Hung
 * Comp: MDChiShenMe, Inc.
 * Proj: Server
 * Date: 12/9/14
 * Time: 11:17 AM
 */
@Entity
public class Image extends Media{
    public String imageUrl () {
        return Global.getBlobService().getBlobUrl(IBlobService.BUCKET_IMAGE, blobKey);
    }

    public String mediaUrl () {
        return imageUrl();
    }

    public static Model.Finder<Long, Image> finder = new Model.Finder(
            Long.class, Image.class
    );

    public static Image findById (Long imageId) {
        return finder.byId(imageId);
    }

    public static List<Image> findByUserId (Long userId) {
        return finder.where().eq("uploaded_by_id", userId).findList();
    }

    public static List<Image> findByRestaurant (Long restaurantId) {
        return finder.where().eq("restaurant.id", restaurantId).findList();
    }
}
