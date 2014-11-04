package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * User: Ling Hung
 * Comp: XappMedia, Inc.
 * Proj: xapp-server
 * Date: 10/31/14
 * Time: 6:09 PM
 */
@Entity
@Table(
        name="login_session"
)
public class LoginSession extends Model {
    @Id
    @Column(length=36)
    public String sessionUuid;

    @Basic(optional=false) @Temporal(TemporalType.TIMESTAMP)
    public Date createdTimestamp;

    @OneToMany(mappedBy="loginSession")
    public List<LoginSessionKey> sessionKeys;

    @ManyToOne(optional=true)
    public User user;

    public LoginSessionKey key (String key) {
        for (LoginSessionKey tuple : sessionKeys) {
            if (tuple.key.equals(key)) {
                return tuple;
            }
        }

        return null;
    }

    public static Model.Finder<String, LoginSession> finder = new Model.Finder(
            String.class, LoginSession.class
    );

    public static LoginSession findByUuid (String uuid) {
        return finder.byId(uuid);
    }

    public static List<LoginSession> findByAdminId (long userId) {
        return finder.where().eq("user_id", userId).findList();
    }
}
