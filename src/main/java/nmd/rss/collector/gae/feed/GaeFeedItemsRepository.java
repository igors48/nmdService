package nmd.rss.collector.gae.feed;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import nmd.rss.collector.feed.FeedItem;
import nmd.rss.collector.updater.FeedItemsRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.lang.reflect.Type;
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
    public void storeItems(UUID feedId, List<FeedItem> feedItems) {
        assertNotNull(feedId);
        assertNotNull(feedItems);

        final Query query = this.entityManager.createQuery("DELETE FROM FeedItemEntity feedItemEntity WHERE feedItemEntity.feedId = :feedId");
        query.setParameter("feedId", feedId.toString());
        query.executeUpdate();

        final List<FeedItemHelper> helpers = new ArrayList<>();

        for (final FeedItem current : feedItems) {
            helpers.add(FeedItemHelper.convert(current));
        }

        String json = new Gson().toJson(helpers);
        FeedItemEntity entity = new FeedItemEntity(feedId, json);

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

        if (!entities.isEmpty()) {
            Type collectionType = new TypeToken<ArrayList<FeedItemHelper>>() {
            }.getType();
            final List<FeedItemHelper> helpers = new Gson().fromJson(entities.get(0).getData().getValue(), collectionType);

            for (final FeedItemHelper helper : helpers) {
                final FeedItem item = FeedItemHelper.convert(helper);

                result.add(item);
            }
        }
        return result;
    }

}
