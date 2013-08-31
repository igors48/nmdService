package nmd.rss.collector.gae.feed.item;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;
import java.util.UUID;

import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.collector.util.Assert.assertPositive;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 03.05.13
 */
@Entity(name = FeedItemsEntity.NAME)
public class FeedItemsEntity {

    public static final String NAME = "FeedItems";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;

    private String feedId;
    private int itemsCount;
    private Date lastUpdate;
    private Text data;

    protected FeedItemsEntity() {
        // empty
    }

    public FeedItemsEntity(final UUID feedId, final int itemsCount, final String data, final Date lastUpdate) {
        assertNotNull(feedId);
        this.feedId = feedId.toString();

        assertPositive(itemsCount);
        this.itemsCount = itemsCount;

        assertNotNull(data);
        this.data = new Text(data);

        assertNotNull(lastUpdate);
        this.lastUpdate = lastUpdate;
    }

    public Date getLastUpdate() {
        return this.lastUpdate;
    }

    public Text getData() {
        return this.data;
    }

}
