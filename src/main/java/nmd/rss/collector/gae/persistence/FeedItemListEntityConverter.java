package nmd.rss.collector.gae.persistence;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import nmd.rss.collector.feed.FeedItem;

import java.util.List;
import java.util.UUID;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * User: igu
 * Date: 21.10.13
 */
public class FeedItemListEntityConverter {

    public static final String KIND = "FeedItem";

    public static final String FEED_ID = "feedId";
    public static final String ITEMS = "items";

    public static Entity convert(final Key feedKey, final UUID feedId, final List<FeedItem> feedItems) {
        assertNotNull(feedId);
        assertNotNull(feedItems);

        final Entity entity = new Entity(KIND, feedKey);

        entity.setProperty(FEED_ID, feedId.toString());
        entity.setProperty(ITEMS, FeedItemListConverter.convert(feedItems));

        return entity;
    }

    public static List<FeedItem> convert(final Entity entity) {
        assertNotNull(entity);

        final String items = (String) entity.getProperty(ITEMS);

        return FeedItemListConverter.convert(items);
    }

}
