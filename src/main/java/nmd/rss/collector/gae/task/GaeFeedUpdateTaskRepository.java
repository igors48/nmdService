package nmd.rss.collector.gae.task;

import nmd.rss.collector.scheduler.FeedUpdateTask;
import nmd.rss.collector.scheduler.FeedUpdateTaskRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 31.05.13
 */
public class GaeFeedUpdateTaskRepository implements FeedUpdateTaskRepository {

    private final EntityManager entityManager;

    public GaeFeedUpdateTaskRepository(final EntityManager entityManager) {
        assertNotNull(entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public List<FeedUpdateTask> loadAllTasks() {
        final TypedQuery<FeedUpdateTaskEntity> query = this.entityManager.createQuery("SELECT entity FROM FeedUpdateTask entity", FeedUpdateTaskEntity.class);

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

        final Query query = this.entityManager.createQuery("DELETE FROM FeedUpdateTask entity WHERE entity.id = :id");
        query.setParameter("id", feedUpdateTask.id.toString());
        query.executeUpdate();

        final FeedUpdateTaskEntity entity = FeedUpdateTaskEntity.convert(feedUpdateTask);
        this.entityManager.persist(entity);
    }

}
