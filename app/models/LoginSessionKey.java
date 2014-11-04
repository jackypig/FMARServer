package models;

import play.db.ebean.Model;

import javax.persistence.*;

/**
 * User: Ling Hung
 * Comp: XappMedia, Inc.
 * Proj: xapp-server
 * Date: 10/31/14
 * Time: 6:11 PM
 */
@Entity
@Table(
        name="admin_session_key",
        uniqueConstraints=@UniqueConstraint(columnNames={"session_uuid", "session_key"})
)
public class LoginSessionKey extends Model {
    @Id
    public Long id;

    @ManyToOne() @JoinColumn(name="session_uuid")
    public LoginSession loginSession;

    @Basic(optional=false) @Column(name="session_key")
    public String key;

    @Basic(optional=false)
    public String value;
}
