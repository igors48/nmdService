package nmd.rss.collector.gae.persistence;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import nmd.rss.collector.scheduler.FeedUpdateTask;

import java.util.UUID;

import static nmd.rss.collector.gae.persistence.Kind.FEED_UPDATE_TASK;
import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * User: igu
 * Date: 16.10.13
 */
public class FeedUpdateTaskEntityConverter {

    private static final String FEED_ID = "feedId";
    private static final String MAX_FEED_ITEMS_COUNT = "maxFeedItemsCount";

    public static Entity convert(final FeedUpdateTask updateTask, final Key feedKey) {
        assertNotNull(updateTask);
        assertNotNull(feedKey);

        final Entity entity = new Entity(FEED_UPDATE_TASK.value, feedKey);

        entity.setProperty(FEED_ID, updateTask.feedId.toString());
        entity.setProperty(MAX_FEED_ITEMS_COUNT, updateTask.maxFeedItemsCount);

        return entity;
    }

    public static FeedUpdateTask convert(final Entity entity) {
        assertNotNull(entity);

        final UUID feedId = UUID.fromString((String) entity.getProperty(FEED_ID));
        //TODO nice hack
        final int maxFeedItemsCount = ((Number) (entity.getProperty(MAX_FEED_ITEMS_COUNT))).intValue();

        return new FeedUpdateTask(feedId, maxFeedItemsCount);
    }

}
