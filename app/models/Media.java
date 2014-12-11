package models;

import com.avaje.ebean.ExpressionList;
import play.Logger;
import play.db.ebean.Model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.Date;

/**
 * User: Ling Hung
 * Comp: MDChiShenMe, Inc.
 * Proj: Server
 * Date: 12/9/14
 * Time: 4:08 PM
 */
@MappedSuperclass
public abstract class Media extends Model{
    @Id
    public long id;

    @Basic(optional=true)
    public String blobKey;

    @Basic(optional=true)
    public String filename;

    @Basic(optional=true)
    public Long fileLength;

    @Basic(optional=false) @Enumerated(EnumType.STRING)
    public MediaStatus status;

    @Basic(optional=false) @Enumerated(EnumType.STRING)    @Column(length=20)
    public MimeType mimeType;

    @Basic(optional=false)
    public Date createdTimestamp;

    @ManyToOne(optional=false)
    public User uploadedBy;

    public abstract String mediaUrl ();

    public String getFilename () {
        return filename;
    }
}
