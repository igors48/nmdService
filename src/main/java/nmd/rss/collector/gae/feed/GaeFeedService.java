package nmd.rss.collector.gae.feed;

import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.collector.feed.FeedItem;
import nmd.rss.collector.updater.FeedService;
import nmd.rss.collector.updater.FeedServiceException;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.UUID;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 02.05.13
 */
public class GaeFeedService implements FeedService {

    private final EntityManager entityManager;
    private final GaeFeedItemsRepository feedItemsRepository;

    public GaeFeedService(final EntityManager entityManager, final GaeFeedItemsRepository feedItemsRepository) {
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

        EntityTransaction transaction = this.entityManager.getTransaction();

        try {

        } catch (Exception exception) {

        } finally {

        }

        return null;
    }

    @Override
    public void updateItems(final UUID feedId, final List<FeedItem> removed, final List<FeedItem> retained, final List<FeedItem> added) throws FeedServiceException {
        assertNotNull(feedId);
        assertNotNull(removed);
        assertNotNull(retained);
        assertNotNull(added);
    }
}
