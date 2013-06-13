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
import java.util.List;
import java.util.UUID;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 03.05.13
 */
public class GaeFeedItemsRepository extends AbstractGaeRepository implements FeedItemsRepository {

    private static final String FEED_ID = "feedId";

    public GaeFeedItemsRepository(final EntityManager entityManager) {
        super(entityManager, FeedItemsEntity.NAME);
    }

    @Override
    public void updateItems(final UUID feedId, final List<FeedItem> feedItems) {
        assertNotNull(feedId);
        assertNotNull(feedItems);

        deleteItems(feedId);

        final List<FeedItemHelper> helpers = new ArrayList<>();

        for (final FeedItem current : feedItems) {
            helpers.add(FeedItemHelper.convert(current));
        }

        final String data = new Gson().toJson(helpers);
        final FeedItemsEntity entity = new FeedItemsEntity(feedId, data);

        persist(entity);
    }

    @Override
    public List<FeedItem> loadItems(final UUID feedId) {
        assertNotNull(feedId);

        final TypedQuery<FeedItemsEntity> query = buildSelectWhereQuery(FEED_ID, feedId.toString(), FeedItemsEntity.class);

        final List<FeedItemsEntity> entities = query.getResultList();

        return createFeedItems(entities);
    }

    @Override
    public void deleteItems(final UUID feedId) {
        assertNotNull(feedId);

        final Query query = buildDeleteWhereQuery(FEED_ID, feedId.toString());

        query.executeUpdate();
    }

    private List<FeedItem> createFeedItems(final List<FeedItemsEntity> entities) {
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
