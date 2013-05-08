package nmd.rss.collector.gae.feed;

import nmd.rss.collector.feed.FeedItem;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
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

    public GaeFeedItemsRepository(final EntityManager entityManager) {
        assertNotNull(entityManager);
        this.entityManager = entityManager;
    }

    public int removeItem(final UUID feedItemId) {
        assertNotNull(feedItemId);

        final Query query = this.entityManager.createQuery("DELETE FROM FeedItemEntity feedItemEntity WHERE feedItemEntity.id = :feedItemId");
        query.setParameter("feedItemId", feedItemId);

        return query.executeUpdate();
    }

    public void addItem(final UUID feedId, final FeedItem feedItem) {
        assertNotNull(feedId);
        assertNotNull(feedItem);

        final FeedItemEntity entity = FeedItemEntity.convert(feedId, feedItem);

        this.entityManager.persist(entity);
    }

    public List<FeedItem> loadItems(final UUID feedId) {
        assertNotNull(feedId);

        List<FeedItem> result = new ArrayList<>();

        final TypedQuery<FeedItemEntity> query = this.entityManager.createQuery("SELECT feedItemEntity FROM FeedItemEntity feedItemEntity WHERE feedItemEntity.feedId = :feedId", FeedItemEntity.class);
        query.setParameter("feedId", feedId);

        final List<FeedItemEntity> entities = query.getResultList();

        for (final FeedItemEntity entity : entities) {
            final FeedItem item = FeedItemEntity.convert(entity);

            result.add(item);
        }

        return result;
    }

}
