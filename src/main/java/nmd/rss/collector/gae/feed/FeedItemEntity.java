package nmd.rss.collector.gae.feed;

import com.google.appengine.api.datastore.Text;
import nmd.rss.collector.feed.FeedItem;

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
@Entity
public class FeedItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private com.google.appengine.api.datastore.Key key;

    private String id;
    private String feedId;
    private Text title;
    private Text description;
    private Text link;
    private long timestamp;

    protected FeedItemEntity() {

    }

    public FeedItemEntity(UUID id, UUID feedId, String title, String description, String link, long timestamp) {
        this.id = id.toString();
        this.feedId = feedId.toString();
        this.title = new Text(title);
        this.description = new Text(description);
        this.link = new Text(link);
        this.timestamp = timestamp;
    }

    public String getId() {
        return this.id;
    }

    public String getFeedId() {
        return this.feedId;
    }

    public Text getTitle() {
        return this.title;
    }

    public Text getDescription() {
        return this.description;
    }

    public Text getLink() {
        return this.link;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public static FeedItem convert(final FeedItemEntity entity) {
        assertNotNull(entity);

        return new FeedItem(UUID.fromString(entity.getId()), entity.getTitle().getValue(), entity.getDescription().getValue(), entity.getLink().getValue(), entity.getTimestamp());
    }

    public static FeedItemEntity convert(final UUID feedId, final FeedItem item) {
        assertNotNull(feedId);
        assertNotNull(item);

        return new FeedItemEntity(item.id, feedId, item.title, item.description, item.link, item.timestamp);
    }
}
