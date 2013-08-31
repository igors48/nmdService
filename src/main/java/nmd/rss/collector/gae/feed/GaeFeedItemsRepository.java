package nmd.rss.collector.gae.feed;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import nmd.rss.collector.AbstractGaeRepository;
import nmd.rss.collector.feed.FeedItem;
import nmd.rss.collector.gae.feed.item.FeedItemHelper;
import nmd.rss.collector.gae.feed.item.FeedItemsEntity;
import nmd.rss.collector.updater.FeedItemsRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 03.05.13
 */
public class GaeFeedItemsRepository extends AbstractGaeRepository implements FeedItemsRepository {

    private static final String FEED_ID = "feedId";
    private static final Type LIST_TYPE = new TypeToken<ArrayList<FeedItemHelper>>() {
    }.getType();

    public GaeFeedItemsRepository(final EntityManager entityManager) {
        super(entityManager, FeedItemsEntity.NAME);
    }

    @Override
    public void updateItems(final UUID feedId, final List<FeedItem> feedItems) {
        assertNotNull(feedId);
        assertNotNull(feedItems);

        final List<FeedItemsEntity> oldItems = loadEntitiesForFeedId(feedId);

        final FeedItemsEntity entity = createEntity(feedId, feedItems);

        persist(entity);

        deleteOldItems(oldItems);
    }

    @Override
    public List<FeedItem> loadItems(final UUID feedId) {
        assertNotNull(feedId);

        final List<FeedItemsEntity> entities = loadEntitiesForFeedId(feedId);

        return createFeedItems(entities);
    }

    @Override
    public void deleteItems(final UUID feedId) {
        assertNotNull(feedId);

        final Query query = buildDeleteWhereQuery(FEED_ID, feedId.toString());

        query.executeUpdate();
    }

    private List<FeedItemsEntity> loadEntitiesForFeedId(final UUID feedId) {
        final TypedQuery<FeedItemsEntity> query = buildSelectWhereQuery(FEED_ID, feedId.toString(), FeedItemsEntity.class);

        return query.getResultList();
    }

    private List<FeedItem> createFeedItems(final List<FeedItemsEntity> entities) {
        final List<FeedItem> result = new ArrayList<>();

        if (!entities.isEmpty()) {
            final FeedItemsEntity mostRecentEntity = findMostRecentEntity(entities);
            final List<FeedItemHelper> helpers = new Gson().fromJson(mostRecentEntity.getData().getValue(), LIST_TYPE);

            for (final FeedItemHelper helper : helpers) {
                final FeedItem item = FeedItemHelper.convert(helper);

                result.add(item);
            }
        }

        return result;
    }

    private FeedItemsEntity findMostRecentEntity(final List<FeedItemsEntity> entities) {
        int index = 0;
        long maxTimeStamp = 0;

        int current = 0;

        for (final FeedItemsEntity entity : entities) {
            final long timeStamp = entity.getLastUpdate().getTime();

            if (timeStamp > maxTimeStamp) {
                maxTimeStamp = timeStamp;
                index = current;
            }

            ++current;
        }

        return entities.get(index);
    }

    private void deleteOldItems(final List<FeedItemsEntity> oldItems) {

        if (oldItems.isEmpty()) {
            return;
        }

        final List<FeedItemsEntity> victims = oldItems.subList(0, oldItems.size() > 1 ? 2 : 1);

        for (final FeedItemsEntity victim : victims) {
            remove(victim);
        }
    }

    private FeedItemsEntity createEntity(final UUID feedId, final List<FeedItem> feedItems) {
        final List<FeedItemHelper> helpers = new ArrayList<>();

        for (final FeedItem current : feedItems) {
            helpers.add(FeedItemHelper.convert(current));
        }

        final String data = new Gson().toJson(helpers);
        return new FeedItemsEntity(feedId, feedItems.size(), data, new Date());
    }

}
