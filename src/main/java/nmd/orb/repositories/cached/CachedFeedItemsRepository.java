package nmd.orb.repositories.cached;

import nmd.orb.feed.FeedItem;
import nmd.orb.feed.FeedItemShortcut;
import nmd.orb.repositories.Cache;
import nmd.orb.repositories.FeedItemsRepository;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import static nmd.orb.feed.FeedItem.createShortcuts;
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

        putToCacheAndGetShortcuts(feedId, items);
    }

    @Override
    public synchronized List<FeedItem> loadItems(final UUID feedId) {
        assertNotNull(feedId);

        final List<FeedItem> cached = (List<FeedItem>) this.cache.get(keyForItems(feedId));

        return cached == null ? cacheItems(feedId) : cached;
    }

    @Override
    public List<FeedItemShortcut> loadItemsShortcuts(UUID feedId) {
        assertNotNull(feedId);

        final List<FeedItemShortcut> cached = (List<FeedItemShortcut>) this.cache.get(keyForShortcuts(feedId));

        return cached == null ? cacheShortcuts(feedId) : cached;
    }

    @Override
    public synchronized void deleteItems(final UUID feedId) {
        assertNotNull(feedId);

        this.cache.delete(keyForItems(feedId));
        this.cache.delete(keyForShortcuts(feedId));

        this.feedItemsRepository.deleteItems(feedId);
    }

    private List<FeedItem> cacheItems(UUID feedId) {
        final List<FeedItem> loaded = this.feedItemsRepository.loadItems(feedId);

        putToCacheAndGetShortcuts(feedId, loaded);

        LOGGER.info(String.format("Items for feed [ %s ] were loaded from datastore", feedId));

        return loaded;
    }

    private List<FeedItemShortcut> cacheShortcuts(UUID feedId) {
        final List<FeedItem> loaded = this.feedItemsRepository.loadItems(feedId);

        LOGGER.info(String.format("Items for feed [ %s ] were loaded from datastore", feedId));

        return putToCacheAndGetShortcuts(feedId, loaded);
    }

    private List<FeedItemShortcut> putToCacheAndGetShortcuts(final UUID feedId, final List<FeedItem> items) {
        final List<FeedItemShortcut> shortcuts = createShortcuts(items);

        this.cache.put(keyForItems(feedId), items);
        this.cache.put(keyForShortcuts(feedId), shortcuts);

        return shortcuts;
    }

    public static String keyForItems(final UUID uuid) {
        assertNotNull(uuid);

        return "FEED-" + uuid;
    }

    public static String keyForShortcuts(final UUID uuid) {
        assertNotNull(uuid);

        return "SHORTCUT-" + uuid;
    }

}
