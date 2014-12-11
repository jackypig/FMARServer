package models;

import play.db.ebean.Model;

import javax.persistence.*;

/**
 * User: Ling Hung
 * Comp: MDChiShenMe, Inc.
 * Proj: Server
 * Date: 12/9/14
 * Time: 4:04 PM
 */
@Entity
@Table(name="blob_holder")
public class Blob extends Model {

    @Basic(optional=false)
    public String bucket;

    @Basic(optional=false) @Column(name="blob_key")
    public String key;

    @Basic(optional=false) @Enumerated(EnumType.STRING)
    public MimeType mimeType;

    @Lob
    public byte [] data;
}
