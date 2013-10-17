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
import static nmd.rss.collector.gae.persistence.FeedUpdateTaskConverter.KIND;
import static nmd.rss.collector.gae.persistence.RootRepository.DATASTORE_SERVICE;
import static nmd.rss.collector.gae.persistence.RootRepository.getFeedRootKey;
import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * User: igu
 * Date: 16.10.13
 */
public class NewFeedUpdateTaskRepository implements FeedUpdateTaskRepository {

    @Override
    public List<FeedUpdateTask> loadAllTasks() {
        final Query query = new Query(KIND);
        final PreparedQuery preparedQuery = DATASTORE_SERVICE.prepare(query);

        final List<Entity> entities = preparedQuery.asList(withLimit(MAX_VALUE));

        final List<FeedUpdateTask> headers = new ArrayList<>(entities.size());

        for (final Entity entity : entities) {
            final FeedUpdateTask feedUpdateTask = FeedUpdateTaskConverter.convert(entity);

            headers.add(feedUpdateTask);
        }

        return headers;
    }

    @Override
    public void storeTask(FeedUpdateTask feedUpdateTask) {
        assertNotNull(feedUpdateTask);

        final Key feedRootKey = getFeedRootKey(feedUpdateTask.feedId);
        final Entity entity = FeedUpdateTaskConverter.convert(feedUpdateTask, feedRootKey);

        DATASTORE_SERVICE.put(entity);
    }

    @Override
    public FeedUpdateTask loadTaskForFeedId(UUID feedId) {
        assertNotNull(feedId);

        final Entity entity = getEntity(feedId);

        return entity == null ? null : FeedUpdateTaskConverter.convert(entity);
    }

    @Override
    public void deleteTaskForFeedId(UUID feedId) {
        assertNotNull(feedId);

        final Entity entity = getEntity(feedId);

        DATASTORE_SERVICE.delete(entity.getKey());
    }

    private Entity getEntity(UUID feedId) {
        final Query query = new Query(KIND).setAncestor(getFeedRootKey(feedId));
        final PreparedQuery preparedQuery = DATASTORE_SERVICE.prepare(query);

        return preparedQuery.asSingleEntity();
    }

}
