package nmd.rss.collector.gae.feed;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.UUID;

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
    private String title;
    private String description;
    private String link;
    private long timestamp;

    protected FeedItemEntity() {

    }

    public FeedItemEntity(UUID id, String title, String description, String link, long timestamp) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.link = link;
        this.timestamp = timestamp;
    }

    public UUID getId() {
        return this.id;
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

}
