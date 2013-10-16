package nmd.rss.collector.gae.persistence;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import nmd.rss.collector.scheduler.FeedUpdateTask;

import java.util.UUID;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * User: igu
 * Date: 16.10.13
 */
public class FeedUpdateTaskConverter {

    public static final String KIND = "FeedUpdateTask";

    private static final String FEED_ID = "feedId";
    private static final String MAX_FEED_ITEMS_COUNT = "maxFeedItemsCount";

    public static Entity convert(FeedUpdateTask updateTask, Key feedKey) {
        assertNotNull(updateTask);
        assertNotNull(feedKey);

        final Entity entity = new Entity(KIND, feedKey);

        entity.setProperty(FEED_ID, updateTask.feedId);
        entity.setProperty(MAX_FEED_ITEMS_COUNT, updateTask.maxFeedItemsCount);

        return entity;
    }

    public static FeedUpdateTask convert(Entity entity) {
        assertNotNull(entity);

        final UUID feedId = UUID.fromString((String) entity.getProperty(FEED_ID));
        final int maxFeedItemsCount = (Integer) entity.getProperty(MAX_FEED_ITEMS_COUNT);

        return new FeedUpdateTask(feedId, maxFeedItemsCount);
    }

}
