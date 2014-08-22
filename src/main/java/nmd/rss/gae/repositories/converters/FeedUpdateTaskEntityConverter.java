package nmd.rss.gae.repositories.converters;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.gson.Gson;
import nmd.rss.collector.scheduler.FeedUpdateTask;

import java.util.UUID;

import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.gae.repositories.datastore.Kind.FEED_UPDATE_TASK;

/**
 * User: igu
 * Date: 16.10.13
 */
public class FeedUpdateTaskEntityConverter {

    private static final Gson GSON = new Gson();

    private static final String FEED_ID = "feedId";
    private static final String MAX_FEED_ITEMS_COUNT = "maxFeedItemsCount";
    private static final String STATISTIC = "statistic";

    public static Entity convert(final FeedUpdateTask updateTask, final Key feedKey) {
        assertNotNull(updateTask);
        assertNotNull(feedKey);

        final Entity entity = new Entity(FEED_UPDATE_TASK.value, feedKey);

        entity.setProperty(FEED_ID, updateTask.feedId.toString());
        entity.setProperty(MAX_FEED_ITEMS_COUNT, updateTask.maxFeedItemsCount);

        final StatisticHelper statisticHelper = new StatisticHelper(updateTask.updates, updateTask.executions);
        final String statisticHelperJson = GSON.toJson(statisticHelper);
        entity.setProperty(STATISTIC, statisticHelperJson);

        return entity;
    }

    public static FeedUpdateTask convert(final Entity entity) {
        assertNotNull(entity);

        final UUID feedId = UUID.fromString((String) entity.getProperty(FEED_ID));
        //TODO nice hack
        final int maxFeedItemsCount = ((Number) (entity.getProperty(MAX_FEED_ITEMS_COUNT))).intValue();

        final StatisticHelper statisticHelper;

        if (entity.hasProperty(STATISTIC)) {
            final String statisticHelperJson = (String) entity.getProperty(STATISTIC);
            statisticHelper = GSON.fromJson(statisticHelperJson, StatisticHelper.class);
        } else {
            statisticHelper = new StatisticHelper();
        }

        return new FeedUpdateTask(feedId, maxFeedItemsCount, statisticHelper.updates, statisticHelper.executions);
    }

    private static class StatisticHelper {

        public int updates;
        public int executions;

        private StatisticHelper() {
            this(0, 0);
        }

        private StatisticHelper(int updates, int executions) {
            this.updates = updates;
            this.executions = executions;
        }

    }

}
