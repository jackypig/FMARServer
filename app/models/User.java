package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.util.List;

/**
 * User: Ling Hung
 * Comp: ChiShenMe, Inc.
 * Proj: xapp-server
 * Date: 10/29/14
 * Time: 8:55 AM
 */
@Entity
@Table(
        name="user",
        uniqueConstraints=@UniqueConstraint(columnNames={"email"})
)
public class User extends Model {
    @Id
    public Long id;

    @Basic(optional=false) @Enumerated(EnumType.STRING)
    public LoginType loginType;

    @Basic(optional=false)
    public String email;

    @Basic(optional=true)
    public String firstName;

    @Basic(optional=true)
    public String lastName;

    @Basic(optional=false)
    public String passwordEncrypted;

    @Basic(optional=false)
    public boolean active;

    public boolean isSuperuser () {
        return loginType == LoginType.SUPER_USER;
    }

    public boolean isRestaurantRep() {
        return loginType == LoginType.RESTAURANT_REPRESENTATIVE;
    }

    public String fullName () {
        return firstName + " " + lastName;
    }

    public static Model.Finder<Long, User> finder = new Model.Finder(
            Long.class, User.class
    );

    public static User findById (Long adminId) {
        return finder.byId(adminId);
    }

    public static User findByEmail (String email) {
        return finder.where().eq("email", email).findUnique();
    }

    public static List<User> findAll () {
        return finder.all();
    }
}
