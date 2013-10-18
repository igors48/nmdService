package feed.converter;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.collector.feed.FeedItem;
import nmd.rss.collector.gae.persistence.FeedHeaderConverter;
import nmd.rss.collector.gae.persistence.FeedItemConverter;
import nmd.rss.collector.gae.persistence.FeedUpdateTaskConverter;
import nmd.rss.collector.scheduler.FeedUpdateTask;
import org.junit.Test;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

/**
 * User: igu
 * Date: 18.10.13
 */
public class ConvertersTest {

    public static final Key SAMPLE_KEY = KeyFactory.stringToKey("ag9zfnJzcy1jb2xsZWN0b3JyHQsSEEZlZWRIZWFkZXJFbnRpdHkYgICAgIi0vwgM");

    @Test
    public void feedHeaderRoundtrip() {
        final FeedHeader origin = new FeedHeader(UUID.randomUUID(), "feedLink", "title", "description", "link");

        final Entity entity = FeedHeaderConverter.convert(origin, SAMPLE_KEY);

        final FeedHeader restored = FeedHeaderConverter.convert(entity);

        assertEquals(origin, restored);
    }

    @Test
    public void feedItemRoundtrip() {
        final FeedItem origin = new FeedItem("title", "description", "link", new Date(), "guid");

        final Entity entity = FeedItemConverter.convert(origin, SAMPLE_KEY, "feedId");

        final FeedItem restored = FeedItemConverter.convert(entity);

        assertEquals(origin, restored);
    }

    @Test
    public void feedUpdateTaskRoundtrip() {
        final FeedUpdateTask origin = new FeedUpdateTask(UUID.randomUUID(), 1000);

        final Entity entity = FeedUpdateTaskConverter.convert(origin, SAMPLE_KEY);

        final FeedUpdateTask restored = FeedUpdateTaskConverter.convert(entity);

        assertEquals(origin, restored);
    }

}
