package nmd.rss.collector.gae.feed;

import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.collector.feed.FeedItem;
import nmd.rss.collector.updater.FeedItemsRepository;
import nmd.rss.collector.updater.FeedService;
import nmd.rss.collector.updater.FeedServiceException;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.UUID;

import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.collector.util.TransactionTools.rollbackIfActive;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 02.05.13
 */
public class FeedServiceImpl implements FeedService {

    private final EntityManager entityManager;
    private final FeedItemsRepository feedItemsRepository;

    public FeedServiceImpl(final EntityManager entityManager, final FeedItemsRepository feedItemsRepository) {
        assertNotNull(entityManager);
        this.entityManager = entityManager;

        assertNotNull(feedItemsRepository);
        this.feedItemsRepository = feedItemsRepository;
    }

    @Override
    public FeedHeader loadHeader(final UUID feedId) throws FeedServiceException {
        assertNotNull(feedId);

        return null;
    }

    @Override
    public List<FeedItem> loadItems(final UUID feedId) throws FeedServiceException {
        assertNotNull(feedId);

        EntityTransaction transaction = null;

        try {
            transaction = this.entityManager.getTransaction();

            final List<FeedItem> result = this.feedItemsRepository.loadItems(feedId);

            transaction.commit();

            return result;
        } catch (Exception exception) {
            throw new FeedServiceException(exception);
        } finally {
            rollbackIfActive(transaction);
        }
    }

    @Override
    public void updateItems(final UUID feedId, final List<FeedItem> removed, final List<FeedItem> retained, final List<FeedItem> added) throws FeedServiceException {
        assertNotNull(feedId);
        assertNotNull(removed);
        assertNotNull(retained);
        assertNotNull(added);

        EntityTransaction transaction = null;

        try {
            transaction = this.entityManager.getTransaction();

            for (final FeedItem victim : removed) {
                this.feedItemsRepository.removeItem(victim.id);
            }

            for (final FeedItem current : added) {
                this.feedItemsRepository.addItem(feedId, current);
            }

            transaction.commit();
        } catch (Exception exception) {
            throw new FeedServiceException(exception);
        } finally {
            rollbackIfActive(transaction);
        }
    }

}
