package nmd.rss.collector.gae.feed.item;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.UUID;

import static nmd.rss.collector.util.Assert.assertNotNull;

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
    private Text data;

    protected FeedItemsEntity() {
        // empty
    }

    public FeedItemsEntity(final UUID feedId, final String data) {
        assertNotNull(feedId);
        this.feedId = feedId.toString();

        assertNotNull(data);
        this.data = new Text(data);
    }

    public String getFeedId() {
        return this.feedId;
    }

    public Text getData() {
        return this.data;
    }

}