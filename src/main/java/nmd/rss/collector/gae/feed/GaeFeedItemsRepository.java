package nmd.rss.collector.gae.feed;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import nmd.rss.collector.AbstractGaeRepository;
import nmd.rss.collector.feed.FeedItem;
import nmd.rss.collector.gae.feed.item.FeedItemHelper;
import nmd.rss.collector.gae.feed.item.FeedItems;
import nmd.rss.collector.updater.FeedItemsRepository;

import javax.persistence.EntityManager;
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
        super(entityManager, FeedItems.class.getSimpleName());
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
        final FeedItems entity = new FeedItems(feedId, data);

        persist(entity);
    }

    @Override
    public List<FeedItem> loadItems(final UUID feedId) {
        assertNotNull(feedId);

        final TypedQuery<FeedItems> query = buildSelectWhereQuery(FEED_ID, FEED_ID, FeedItems.class);
        query.setParameter(FEED_ID, feedId.toString());

        final List<FeedItems> entities = query.getResultList();

        return createFeedItems(entities);
    }

    @Override
    public void deleteItems(final UUID feedId) {
        assertNotNull(feedId);

        final TypedQuery<FeedItems> query = buildDeleteWhereQuery(FEED_ID, FEED_ID, FeedItems.class);
        query.setParameter(FEED_ID, feedId.toString());
        query.executeUpdate();
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
