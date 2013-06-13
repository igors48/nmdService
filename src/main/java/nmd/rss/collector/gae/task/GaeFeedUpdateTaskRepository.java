package nmd.rss.collector.gae.task;

import nmd.rss.collector.AbstractGaeRepository;
import nmd.rss.collector.scheduler.FeedUpdateTask;
import nmd.rss.collector.scheduler.FeedUpdateTaskRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 31.05.13
 */
public class GaeFeedUpdateTaskRepository extends AbstractGaeRepository implements FeedUpdateTaskRepository {

    public GaeFeedUpdateTaskRepository(final EntityManager entityManager) {
        super(entityManager, FeedUpdateTaskEntity.NAME);
    }

    @Override
    public List<FeedUpdateTask> loadAllTasks() {
        final TypedQuery<FeedUpdateTaskEntity> query = buildSelectAllQuery(FeedUpdateTaskEntity.class);

        final List<FeedUpdateTaskEntity> entities = query.getResultList();

        final List<FeedUpdateTask> result = new ArrayList<>();

        for (final FeedUpdateTaskEntity entity : entities) {
            result.add(FeedUpdateTaskEntity.convert(entity));
        }

        return result;
    }

    @Override
    public void storeTask(final FeedUpdateTask feedUpdateTask) {
        assertNotNull(feedUpdateTask);

        final Query query = buildDeleteWhereQuery("id", feedUpdateTask.id.toString());

        query.executeUpdate();

        final FeedUpdateTaskEntity entity = FeedUpdateTaskEntity.convert(feedUpdateTask);
        persist(entity);
    }

    @Override
    public FeedUpdateTask loadTaskForFeedId(final UUID feedId) {
        assertNotNull(feedId);

        final TypedQuery<FeedUpdateTaskEntity> query = buildSelectWhereQuery("feedId", feedId.toString(), FeedUpdateTaskEntity.class);
        final List<FeedUpdateTaskEntity> result = query.getResultList();

        return result.isEmpty() ? null : FeedUpdateTaskEntity.convert(result.get(0));
    }

    @Override
    public void deleteTaskForFeedId(final UUID feedId) {
        assertNotNull(feedId);

        final Query query = buildDeleteWhereQuery("feedId", feedId.toString());

        query.executeUpdate();
    }

}
