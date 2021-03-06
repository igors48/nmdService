package nmd.orb.gae.repositories;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import nmd.orb.collector.scheduler.FeedUpdateTask;
import nmd.orb.gae.repositories.datastore.Datastore;
import nmd.orb.repositories.FeedUpdateTaskRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static nmd.orb.gae.repositories.converters.FeedUpdateTaskEntityConverter.convert;
import static nmd.orb.gae.repositories.datastore.GaeDatastoreTools.*;
import static nmd.orb.gae.repositories.datastore.Kind.FEED_UPDATE_TASK;
import static nmd.orb.gae.repositories.datastore.RootKind.FEED;
import static nmd.orb.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date: 16.10.13
 */
public enum GaeFeedUpdateTaskRepository implements FeedUpdateTaskRepository {

    INSTANCE;

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

        deleteTaskForFeedId(feedUpdateTask.feedId);

        final Key feedRootKey = getEntityRootKey(feedUpdateTask.feedId.toString(), FEED);
        final Entity entity = convert(feedUpdateTask, feedRootKey);

        Datastore.INSTANCE.getDatastoreService().put(entity);
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

}
