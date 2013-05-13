package nmd.rss.collector.gae.feed;

import nmd.rss.collector.feed.FeedItem;
import nmd.rss.collector.updater.FeedItemsRepository;

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
public class GaeFeedItemsRepository implements FeedItemsRepository {

    private final EntityManager entityManager;

    public GaeFeedItemsRepository(final EntityManager entityManager) {
        assertNotNull(entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public int removeItem(final UUID feedItemId) {
        assertNotNull(feedItemId);

        final Query query = this.entityManager.createQuery("DELETE FROM FeedItemEntity feedItemEntity WHERE feedItemEntity.id = :feedItemId");
        query.setParameter("feedItemId", feedItemId.toString());

        return query.executeUpdate();
    }

    @Override
    public void addItem(final UUID feedId, final FeedItem feedItem) {
        assertNotNull(feedId);
        assertNotNull(feedItem);

        final FeedItemEntity entity = FeedItemEntity.convert(feedId, feedItem);

        this.entityManager.persist(entity);
    }

    @Override
    public List<FeedItem> loadItems(final UUID feedId) {
        assertNotNull(feedId);

        final TypedQuery<FeedItemEntity> query = this.entityManager.createQuery("SELECT feedItemEntity FROM FeedItemEntity feedItemEntity WHERE feedItemEntity.feedId = :feedId", FeedItemEntity.class);
        query.setParameter("feedId", feedId.toString());

        final List<FeedItemEntity> entities = query.getResultList();

        return createFeedItems(entities);
    }

    @Override
    public List<FeedItemEntity> loadAllItemsKeys() {
        final TypedQuery<FeedItemEntity> query = this.entityManager.createQuery("SELECT feedItemEntity FROM FeedItemEntity feedItemEntity", FeedItemEntity.class);

        return query.getResultList();
    }

    @Override
    public void removeEntity(final FeedItemEntity victim) {
        assertNotNull(victim);

        this.entityManager.remove(victim);
    }

    private List<FeedItem> createFeedItems(List<FeedItemEntity> entities) {
        final List<FeedItem> result = new ArrayList<>();

        for (final FeedItemEntity entity : entities) {
            final FeedItem item = FeedItemEntity.convert(entity);

            result.add(item);
        }

        return result;
    }

}
