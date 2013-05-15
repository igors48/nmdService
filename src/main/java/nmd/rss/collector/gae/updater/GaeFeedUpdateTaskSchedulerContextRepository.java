package nmd.rss.collector.gae.updater;

import nmd.rss.collector.scheduler.FeedUpdateTaskSchedulerContext;
import nmd.rss.collector.scheduler.FeedUpdateTaskSchedulerContextRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 15.05.13
 */
public class GaeFeedUpdateTaskSchedulerContextRepository implements FeedUpdateTaskSchedulerContextRepository {

    private final EntityManager entityManager;

    public GaeFeedUpdateTaskSchedulerContextRepository(final EntityManager entityManager) {
        assertNotNull(entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public void store(final FeedUpdateTaskSchedulerContext context) {
        assertNotNull(context);

        final SchedulerContext schedulerContext = SchedulerContext.convert(context);

        this.entityManager.persist(schedulerContext);
    }

    @Override
    public FeedUpdateTaskSchedulerContext load() {
        final TypedQuery<SchedulerContext> query = this.entityManager.createQuery("SELECT schedulerContext FROM SchedulerContext schedulerContext LIMIT 1", SchedulerContext.class);

        final List<SchedulerContext> queryResult = query.getResultList();

        return queryResult.isEmpty() ? null : SchedulerContext.convert(queryResult.get(0));
    }

    @Override
    public List loadAllEntities() {
        final Query query = this.entityManager.createQuery("SELECT schedulerContext FROM SchedulerContext schedulerContext");

        return query.getResultList();
    }

    @Override
    public void deleteEntity(final Object victim) {
        assertNotNull(victim);

        this.entityManager.remove(victim);
    }

}
