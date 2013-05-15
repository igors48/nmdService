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
    public void updateItems(final UUID feedId, final List<FeedItem> feedItems) {
        assertNotNull(feedId);
        assertNotNull(feedItems);

        final Query query = this.entityManager.createQuery("DELETE FROM FeedItems feedItems WHERE feedItems.feedId = :feedId");
        query.setParameter("feedId", feedId.toString());
        query.executeUpdate();

        final List<FeedItemHelper> helpers = new ArrayList<>();

        for (final FeedItem current : feedItems) {
            helpers.add(FeedItemHelper.convert(current));
        }

        final String data = new Gson().toJson(helpers);
        final FeedItems entity = new FeedItems(feedId, data);

        this.entityManager.persist(entity);
    }

    @Override
    public List<FeedItem> loadItems(final UUID feedId) {
        assertNotNull(feedId);

        final TypedQuery<FeedItems> query = this.entityManager.createQuery("SELECT feedItems FROM FeedItems feedItems WHERE feedItems.feedId = :feedId", FeedItems.class);
        query.setParameter("feedId", feedId.toString());

        final List<FeedItems> entities = query.getResultList();

        return createFeedItems(entities);
    }

    @Override
    public List loadAllEntities() {
        final Query query = this.entityManager.createQuery("SELECT feedItems FROM FeedItems feedItems");

        return query.getResultList();
    }

    @Override
    public void removeEntity(final Object victim) {
        assertNotNull(victim);

        this.entityManager.remove(victim);
    }

    private List<FeedItem> createFeedItems(final List<FeedItems> entities) {
        final List<FeedItem> result = new ArrayList<>();

        if (!entities.isEmpty()) {
            final Type listType = new TypeToken<ArrayList<FeedItemHelper>>() {
            }.getType();
            final List<FeedItemHelper> helpers = new Gson().fromJson(entities.get(0).getData().getValue(), listType);

            for (final FeedItemHelper helper : helpers) {
                final FeedItem item = FeedItemHelper.convert(helper);

                result.add(item);
            }
        }

        return result;
    }

}
