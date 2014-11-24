package nmd.orb.gae.repositories.converters;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import nmd.orb.collector.scheduler.FeedUpdateTask;

import java.util.UUID;

import static nmd.orb.gae.repositories.datastore.Kind.FEED_UPDATE_TASK;
import static nmd.orb.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
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
