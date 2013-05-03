package nmd.rss.collector.gae.feed;

import nmd.rss.collector.feed.FeedItem;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 03.05.13
 */
public class GaeFeedItemsRepository {

    private final EntityManager entityManager;

    public EntityTransaction openTransaction() {
        return this.entityManager.getTransaction();
    }

    public GaeFeedItemsRepository(final EntityManager entityManager) {
        assertNotNull(entityManager);
        this.entityManager = entityManager;
    }

    public void removeItem(final UUID feedId, final UUID feedItemId) throws GaeFeedItemsRepositoryException {
        assertNotNull(feedId);
        assertNotNull(feedItemId);
        //this.entityManager.
    }

    public void addItem(final UUID feedId, final FeedItem feedItem) throws GaeFeedItemsRepositoryException {
        assertNotNull(feedId);
        assertNotNull(feedItem);
    }

    public List<FeedItem> loadItems(final UUID feedId) throws GaeFeedItemsRepositoryException {
        assertNotNull(feedId);

        return new ArrayList<>();
    }
}
