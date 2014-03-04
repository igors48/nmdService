package nmd.rss.collector.gae.persistence;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import nmd.rss.collector.feed.FeedItem;
import nmd.rss.collector.feed.FeedItemsMergeReport;
import nmd.rss.collector.updater.FeedItemsRepository;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 04.03.14
 */
public class GaeCachedFeedItemsRepository implements FeedItemsRepository {

    private static final Logger LOGGER = Logger.getLogger(GaeCachedFeedItemsRepository.class.getName());

    private static final MemcacheService CACHE = MemcacheServiceFactory.getMemcacheService();

    private final FeedItemsRepository feedItemsRepository;

    public GaeCachedFeedItemsRepository(final FeedItemsRepository feedItemsRepository) {
        assertNotNull(feedItemsRepository);
        this.feedItemsRepository = feedItemsRepository;
    }

    @Override
    public void mergeItems(final UUID feedId, final FeedItemsMergeReport feedItemsMergeReport) {
        assertNotNull(feedId);
        assertNotNull(feedItemsMergeReport);

        final boolean nothingChanged = feedItemsMergeReport.added.isEmpty() && feedItemsMergeReport.removed.isEmpty();

        if (nothingChanged) {
            return;
        }

        CACHE.delete(feedId);

        this.feedItemsRepository.mergeItems(feedId, feedItemsMergeReport);
    }

    @Override
    public List<FeedItem> loadItems(final UUID feedId) {
        assertNotNull(feedId);

        final List<FeedItem> cached = (List<FeedItem>) CACHE.get(feedId);

        if (cached == null) {
            final List<FeedItem> loaded = this.feedItemsRepository.loadItems(feedId);

            CACHE.put(feedId, loaded);

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

        CACHE.delete(feedId);

        this.feedItemsRepository.deleteItems(feedId);
    }

}
