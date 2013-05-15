package nmd.rss.collector.scheduler;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.collector.util.TransactionTools.rollbackIfActive;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 15.05.13
 */
public class FeedUpdateTaskSchedulerContextServiceImpl implements FeedUpdateTaskSchedulerContextService {

    private final EntityManager entityManager;
    private final FeedUpdateTaskSchedulerContextRepository contextRepository;

    public FeedUpdateTaskSchedulerContextServiceImpl(final EntityManager entityManager, final FeedUpdateTaskSchedulerContextRepository contextRepository) {
        assertNotNull(entityManager);
        this.entityManager = entityManager;

        assertNotNull(contextRepository);
        this.contextRepository = contextRepository;
    }

    @Override
    public void store(final FeedUpdateTaskSchedulerContext context) throws FeedUpdateTaskSchedulerContextServiceException {
        assertNotNull(context);

        clear();

        EntityTransaction transaction = null;

        try {
            transaction = this.entityManager.getTransaction();
            transaction.begin();

            this.contextRepository.store(context);

            transaction.commit();
        } catch (Exception exception) {
            throw new FeedUpdateTaskSchedulerContextServiceException(exception);
        } finally {
            rollbackIfActive(transaction);
        }
    }

    @Override
    public FeedUpdateTaskSchedulerContext load() throws FeedUpdateTaskSchedulerContextServiceException {
        EntityTransaction transaction = null;

        try {
            transaction = this.entityManager.getTransaction();
            transaction.begin();

            FeedUpdateTaskSchedulerContext result = this.contextRepository.load();

            transaction.commit();

            return result;
        } catch (Exception exception) {
            throw new FeedUpdateTaskSchedulerContextServiceException(exception);
        } finally {
            rollbackIfActive(transaction);
        }
    }

    private void clear() {
        EntityTransaction transaction = null;

        try {
            transaction = this.entityManager.getTransaction();
            transaction.begin();

            List victims = this.contextRepository.loadAllEntities();

            transaction.commit();

            for (Object victim : victims) {
                transaction = this.entityManager.getTransaction();
                transaction.begin();

                this.contextRepository.deleteEntity(victim);

                transaction.commit();
            }
        } finally {
            rollbackIfActive(transaction);
        }
    }

}
