package nmd.orb.collector.updater.cached;

import nmd.orb.collector.Cache;
import nmd.orb.collector.updater.FeedItemsRepository;
import nmd.orb.feed.FeedItem;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import static nmd.orb.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 04.03.14
 */
public class CachedFeedItemsRepository implements FeedItemsRepository {

    private static final Logger LOGGER = Logger.getLogger(CachedFeedItemsRepository.class.getName());

    private final FeedItemsRepository feedItemsRepository;
    private final Cache cache;

    public CachedFeedItemsRepository(final FeedItemsRepository feedItemsRepository, final Cache cache) {
        assertNotNull(feedItemsRepository);
        this.feedItemsRepository = feedItemsRepository;

        assertNotNull(cache);
        this.cache = cache;
    }

    @Override
    public synchronized void storeItems(UUID feedId, List<FeedItem> items) {
        assertNotNull(feedId);
        assertNotNull(items);

        this.feedItemsRepository.storeItems(feedId, items);

        this.cache.put(keyFor(feedId), items);
    }

    @Override
    public synchronized List<FeedItem> loadItems(final UUID feedId) {
        assertNotNull(feedId);

        final List<FeedItem> cached = (List<FeedItem>) this.cache.get(keyFor(feedId));

        if (cached == null) {
            final List<FeedItem> loaded = this.feedItemsRepository.loadItems(feedId);

            this.cache.put(keyFor(feedId), loaded);

            LOGGER.info(String.format("Items for feed [ %s ] were loaded from datastore", feedId));

            return loaded;
        } else {
            return cached;
        }
    }

    @Override
    public synchronized void deleteItems(final UUID feedId) {
        assertNotNull(feedId);

        this.cache.delete(keyFor(feedId));

        this.feedItemsRepository.deleteItems(feedId);
    }

    public static String keyFor(final UUID uuid) {
        assertNotNull(uuid);

        return "FEED-" + uuid;
    }

}
