package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * User: Ling Hung
 * Comp: FetchMeARestaurant, Inc.
 * Proj: fmar-server
 * Date: 10/25/14
 * Time: 4:07 PM
 */
@Entity
@Table(
        name="restaurant"
)
public class Restaurant extends Model implements Comparable<Restaurant> {

    @Id
    public Long id;

    @Basic(optional=false)
    public String address;

    @Basic(optional=false)
    public String category;

    @Basic(optional=false)
    public String city;

    @Basic(optional=false)
    public String englishName;

    @Basic(optional=true)
    public String foreignName;

    @Basic(optional=false)
    public String state;

    @Basic(optional=false)
    public String telephone;

    @Basic(optional=true) @Temporal(TemporalType.TIMESTAMP)
    public Date createdTimestamp;

    public static Model.Finder<Long, Restaurant> finder = new Model.Finder(
            Long.class, Restaurant.class
    );

    public static Restaurant findById (Long restaurantId) {
        Restaurant restaurant = finder.byId(restaurantId);
        return restaurant;
    }

    public static Restaurant findByName(String name) {
        return finder.where().eq("name", name).findUnique();
    }

    public static List<Restaurant> findAll() {
        return finder.all();
    }

    @Override
    public int compareTo(Restaurant restaurant) {
        if (restaurant.englishName != this.englishName) {
            return this.englishName.compareTo(restaurant.englishName);
        }

        return this.englishName.compareToIgnoreCase(restaurant.englishName);
    }
}
