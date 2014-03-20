package nmd.rss.collector.gae.persistence;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import nmd.rss.collector.scheduler.FeedUpdateTask;
import nmd.rss.collector.scheduler.FeedUpdateTaskRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static nmd.rss.collector.gae.persistence.FeedUpdateTaskEntityConverter.convert;
import static nmd.rss.collector.gae.persistence.GaeRootRepository.*;
import static nmd.rss.collector.gae.persistence.Kind.FEED_UPDATE_TASK;
import static nmd.rss.collector.gae.persistence.RootKind.FEED;
import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * User: igu
 * Date: 16.10.13
 */
public class GaeFeedUpdateTaskRepository implements FeedUpdateTaskRepository {

    public static final FeedUpdateTaskRepository GAE_FEED_UPDATE_TASK_REPOSITORY = new GaeFeedUpdateTaskRepository();

    @Override
    public List<FeedUpdateTask> loadAllTasks() {
        final List<Entity> entities = loadEntities(FEED_UPDATE_TASK);

        final List<FeedUpdateTask> tasks = new ArrayList<>(entities.size());

        for (final Entity entity : entities) {
            final FeedUpdateTask feedUpdateTask = convert(entity);

            tasks.add(feedUpdateTask);
        }

        return tasks;
    }

    @Override
    public void storeTask(final FeedUpdateTask feedUpdateTask) {
        assertNotNull(feedUpdateTask);

        final Key feedRootKey = getEntityRootKey(feedUpdateTask.feedId.toString(), FEED);
        final Entity entity = convert(feedUpdateTask, feedRootKey);

        DATASTORE_SERVICE.put(entity);
    }

    @Override
    public FeedUpdateTask loadTaskForFeedId(final UUID feedId) {
        assertNotNull(feedId);

        final Entity entity = loadEntity(feedId.toString(), FEED, FEED_UPDATE_TASK, false);

        return entity == null ? null : convert(entity);
    }

    @Override
    public void deleteTaskForFeedId(final UUID feedId) {
        assertNotNull(feedId);

        deleteEntity(feedId.toString(), FEED, FEED_UPDATE_TASK);
    }

    private GaeFeedUpdateTaskRepository() {
        // empty
    }

}
