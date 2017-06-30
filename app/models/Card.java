package models;

import com.avaje.ebean.annotation.CreatedTimestamp;
import play.db.ebean.Model;

import javax.persistence.Basic;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

/**
 * Created by Ling on 6/26/17.
 */
public class Card extends Model {
    @Id
    public Long id;

    @Basic(optional=false)
    public Bank bank;

    @Basic(optional=false)
    public CardType type;

    @Basic(optional=false) @Temporal(TemporalType.TIMESTAMP)
    @CreatedTimestamp
    public Date createdTimestamp;

    public static Model.Finder<Long, Card> finder = new Model.Finder(
            Long.class, Card.class
    );

    public static Card findById (Long cardId) {
        Card card = finder.byId(cardId);
        return card;
    }

    public static List<Card> findAll() {
        return finder.all();
    }
}
