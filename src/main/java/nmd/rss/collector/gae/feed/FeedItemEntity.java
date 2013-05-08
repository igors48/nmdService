package nmd.rss.collector.gae.feed;

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

    private UUID id;
    private UUID feedId;
    private String title;
    private String description;
    private String link;
    private long timestamp;

    protected FeedItemEntity() {

    }

    public FeedItemEntity(UUID id, UUID feedId, String title, String description, String link, long timestamp) {
        this.id = id;
        this.feedId = feedId;
        this.title = title;
        this.description = description;
        this.link = link;
        this.timestamp = timestamp;
    }

    public UUID getId() {
        return this.id;
    }

    public UUID getFeedId() {
        return this.feedId;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public String getLink() {
        return this.link;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public static FeedItem convert(final FeedItemEntity entity) {
        assertNotNull(entity);

        return new FeedItem(entity.getId(), entity.getTitle(), entity.getDescription(), entity.getLink(), entity.getTimestamp());
    }

    public static FeedItemEntity convert(final UUID feedId, final FeedItem item) {
        assertNotNull(feedId);
        assertNotNull(item);

        return new FeedItemEntity(item.id, feedId, item.title, item.description, item.link, item.timestamp);
    }
}
