package nmd.rss.collector.gae.persistence;

import nmd.rss.collector.Cache;
import nmd.rss.collector.feed.FeedItem;
import nmd.rss.collector.feed.FeedItemsMergeReport;
import nmd.rss.collector.updater.FeedItemsRepository;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import static nmd.rss.collector.gae.cache.GaeCache.GAE_CACHE;
import static nmd.rss.collector.gae.persistence.GaeFeedItemsRepository.GAE_FEED_ITEMS_REPOSITORY;
import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 04.03.14
 */
public class GaeCachedFeedItemsRepository implements FeedItemsRepository {

    public static final FeedItemsRepository GAE_CACHED_FEED_ITEMS_REPOSITORY = new GaeCachedFeedItemsRepository(GAE_FEED_ITEMS_REPOSITORY, GAE_CACHE);

    private static final Logger LOGGER = Logger.getLogger(GaeCachedFeedItemsRepository.class.getName());

    private final FeedItemsRepository feedItemsRepository;
    private final Cache cache;

    private GaeCachedFeedItemsRepository(final FeedItemsRepository feedItemsRepository, final Cache cache) {
        this.feedItemsRepository = feedItemsRepository;
        this.cache = cache;
    }

    @Override
    public void mergeItems(final UUID feedId, final FeedItemsMergeReport feedItemsMergeReport) {
        assertNotNull(feedId);
        assertNotNull(feedItemsMergeReport);

        final boolean nothingChanged = feedItemsMergeReport.added.isEmpty() && feedItemsMergeReport.removed.isEmpty();

        if (nothingChanged) {
            return;
        }

        this.cache.delete(feedId);

        this.feedItemsRepository.mergeItems(feedId, feedItemsMergeReport);
    }

    @Override
    public List<FeedItem> loadItems(final UUID feedId) {
        assertNotNull(feedId);

        final List<FeedItem> cached = (List<FeedItem>) this.cache.get(feedId);

        if (cached == null) {
            final List<FeedItem> loaded = this.feedItemsRepository.loadItems(feedId);

            this.cache.put(feedId, loaded);

            LOGGER.info(String.format("Items for feed [ %s ] were loaded from database", feedId));

            return loaded;
        } else {
            LOGGER.info(String.format("Items for feed [ %s ] were taken from cache", feedId));

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
