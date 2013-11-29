package nmd.rss.collector.gae.persistence;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import nmd.rss.collector.scheduler.FeedUpdateTask;
import nmd.rss.collector.scheduler.FeedUpdateTaskRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import static java.lang.Integer.MAX_VALUE;
import static nmd.rss.collector.gae.persistence.FeedUpdateTaskEntityConverter.KIND;
import static nmd.rss.collector.gae.persistence.GaeRootRepository.DATASTORE_SERVICE;
import static nmd.rss.collector.gae.persistence.GaeRootRepository.getFeedRootKey;
import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * User: igu
 * Date: 16.10.13
 */
public class GaeFeedUpdateTaskRepository implements FeedUpdateTaskRepository {

    @Override
    public List<FeedUpdateTask> loadAllTasks() {
        final Query query = new Query(KIND);
        final PreparedQuery preparedQuery = DATASTORE_SERVICE.prepare(query);

        final List<Entity> entities = preparedQuery.asList(withLimit(MAX_VALUE));

        final List<FeedUpdateTask> tasks = new ArrayList<>(entities.size());

        for (final Entity entity : entities) {
            final FeedUpdateTask feedUpdateTask = FeedUpdateTaskEntityConverter.convert(entity);

            tasks.add(feedUpdateTask);
        }

        return tasks;
    }

    @Override
    public void storeTask(final FeedUpdateTask feedUpdateTask) {
        assertNotNull(feedUpdateTask);

        final Key feedRootKey = getFeedRootKey(feedUpdateTask.feedId);
        final Entity entity = FeedUpdateTaskEntityConverter.convert(feedUpdateTask, feedRootKey);

        DATASTORE_SERVICE.put(entity);
    }

    @Override
    public FeedUpdateTask loadTaskForFeedId(final UUID feedId) {
        assertNotNull(feedId);

        final Entity entity = getEntity(feedId);

        return entity == null ? null : FeedUpdateTaskEntityConverter.convert(entity);
    }

    @Override
    public void deleteTaskForFeedId(final UUID feedId) {
        assertNotNull(feedId);

        final Entity entity = getEntity(feedId);

        if (entity != null) {
            DATASTORE_SERVICE.delete(entity.getKey());
        }
    }

    private Entity getEntity(final UUID feedId) {
        final Query query = new Query(KIND).setAncestor(getFeedRootKey(feedId));
        final PreparedQuery preparedQuery = DATASTORE_SERVICE.prepare(query);

        return preparedQuery.asSingleEntity();
    }

}
