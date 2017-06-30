package models;

import com.avaje.ebean.annotation.CreatedTimestamp;
import play.db.ebean.Model;

import javax.persistence.Basic;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Created by Ling on 6/26/17.
 */
public class Reward extends Model {
    @Id
    public Long id;

    @Basic(optional=false)
    public Card card;

    @Basic(optional=false)
    public RewardType type;

    @Basic(optional=false)
    public RewardCategory category;

    @Basic(optional=false)
    public float amount;

    @Basic(optional=false) @Temporal(TemporalType.TIMESTAMP)
    @CreatedTimestamp
    public Date createdTimestamp;

    public static Model.Finder<Long, Reward> finder = new Model.Finder(
            Long.class, Reward.class
    );

    public static Reward findById (Long cardId) {
        Reward reward = finder.byId(cardId);
        return reward;
    }
}
