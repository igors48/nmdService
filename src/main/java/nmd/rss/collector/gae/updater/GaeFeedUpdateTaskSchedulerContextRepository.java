package nmd.rss.collector.gae.updater;

import nmd.rss.collector.scheduler.FeedUpdateTaskSchedulerContext;
import nmd.rss.collector.scheduler.FeedUpdateTaskSchedulerContextRepository;

import javax.persistence.EntityManager;
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
    }

    @Override
    public FeedUpdateTaskSchedulerContext load() {
        return null;
    }

    @Override
    public List<Object> loadAllEntities() {
        return null;
    }

    @Override
    public void deleteEntity(final Object victim) {
        assertNotNull(victim);
    }

}
