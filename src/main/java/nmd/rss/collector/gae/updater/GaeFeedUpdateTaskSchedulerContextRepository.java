package nmd.rss.collector.gae.updater;

import nmd.rss.collector.AbstractGaeRepository;
import nmd.rss.collector.scheduler.FeedUpdateTaskSchedulerContext;
import nmd.rss.collector.scheduler.FeedUpdateTaskSchedulerContextRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 15.05.13
 */
public class GaeFeedUpdateTaskSchedulerContextRepository extends AbstractGaeRepository implements FeedUpdateTaskSchedulerContextRepository {

    public GaeFeedUpdateTaskSchedulerContextRepository(final EntityManager entityManager) {
        super(entityManager, SchedulerContextEntity.NAME);
    }

    @Override
    public void store(final FeedUpdateTaskSchedulerContext context) {
        assertNotNull(context);

        removeContextIfExists();

        final SchedulerContextEntity entity = SchedulerContextEntity.convert(context);

        persist(entity);
    }

    @Override
    public FeedUpdateTaskSchedulerContext load() {
        final TypedQuery<SchedulerContextEntity> query = buildSelectAllQuery(SchedulerContextEntity.class, 1);

        final List<SchedulerContextEntity> queryResult = query.getResultList();

        return queryResult.isEmpty() ? null : SchedulerContextEntity.convert(queryResult.get(0));
    }

    private void removeContextIfExists() {
        List entities = loadAllEntities();

        if (!entities.isEmpty()) {
            remove(entities.get(0));
        }
    }

}
