package nmd.rss.collector.gae.feed.header;

import com.google.appengine.api.datastore.Key;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import static nmd.rss.collector.util.Assert.assertStringIsValid;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 31.05.13
 */
@Entity(name = FeedHeaderEntity.NAME)
public class FeedHeaderEntity {

    public static final String NAME = "FeedHeader";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;

    private String id;
    private String feedLink;
    private String data;

    protected FeedHeaderEntity() {
        // empty
    }

    public FeedHeaderEntity(final String id, final String feedLink, final String data) {
        assertStringIsValid(id);
        this.id = id;

        assertStringIsValid(feedLink);
        this.feedLink = feedLink;

        assertStringIsValid(data);
        this.data = data;
    }

    public String getData() {
        return this.data;
    }

}
