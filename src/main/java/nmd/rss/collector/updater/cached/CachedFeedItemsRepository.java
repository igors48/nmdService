package nmd.rss.collector.updater.cached;

import nmd.rss.collector.Cache;
import nmd.rss.collector.feed.FeedItem;
import nmd.rss.collector.updater.FeedItemsRepository;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 04.03.14
 */
public class CachedFeedItemsRepository implements FeedItemsRepository {

    private static final Logger LOGGER = Logger.getLogger(CachedFeedItemsRepository.class.getName());

    private final FeedItemsRepository feedItemsRepository;
    private final Cache cache;

    public CachedFeedItemsRepository(final FeedItemsRepository feedItemsRepository, final Cache cache) {
        this.feedItemsRepository = feedItemsRepository;
        this.cache = cache;
    }

    @Override
    public void storeItems(UUID feedId, List<FeedItem> items) {
        assertNotNull(feedId);
        assertNotNull(items);

        this.feedItemsRepository.storeItems(feedId, items);
        this.cache.delete(feedId);
    }

    @Override
    public List<FeedItem> loadItems(final UUID feedId) {
        assertNotNull(feedId);

        final List<FeedItem> cached = (List<FeedItem>) this.cache.get(feedId);

        if (cached == null) {
            final List<FeedItem> loaded = this.feedItemsRepository.loadItems(feedId);

            this.cache.put(feedId, loaded);

            LOGGER.info(String.format("Items for feed [ %s ] were loaded from datastore", feedId));

            return loaded;
        } else {
            return cached;
        }
    }

    @Override
    public void deleteItems(final UUID feedId) {
        assertNotNull(feedId);

        this.cache.delete(feedId);

        this.feedItemsRepository.deleteItems(feedId);
    }

}
