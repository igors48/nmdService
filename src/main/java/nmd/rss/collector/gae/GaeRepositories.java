package nmd.rss.collector.gae;

import nmd.rss.collector.scheduler.FeedUpdateTaskRepository;
import nmd.rss.collector.scheduler.FeedUpdateTaskSchedulerContextRepository;
import nmd.rss.collector.scheduler.cached.CachedFeedUpdateTaskRepository;
import nmd.rss.collector.scheduler.cached.CachedFeedUpdateTaskSchedulerContextRepository;
import nmd.rss.collector.updater.FeedHeadersRepository;
import nmd.rss.collector.updater.FeedItemsRepository;
import nmd.rss.collector.updater.cached.CachedFeedHeadersRepository;
import nmd.rss.collector.updater.cached.CachedFeedItemsRepository;
import nmd.rss.reader.CachedCategoriesRepository;
import nmd.rss.reader.CachedReadFeedItemsRepository;
import nmd.rss.reader.CategoriesRepository;
import nmd.rss.reader.ReadFeedItemsRepository;

import static nmd.rss.collector.gae.cache.MemCache.MEM_CACHE;
import static nmd.rss.collector.gae.persistence.GaeFeedHeadersRepository.GAE_FEED_HEADERS_REPOSITORY;
import static nmd.rss.collector.gae.persistence.GaeFeedItemsRepository.GAE_FEED_ITEMS_REPOSITORY;
import static nmd.rss.collector.gae.persistence.GaeFeedUpdateTaskRepository.GAE_FEED_UPDATE_TASK_REPOSITORY;
import static nmd.rss.reader.gae.GaeCategoriesRepository.GAE_CATEGORIES_REPOSITORY;
import static nmd.rss.reader.gae.GaeReadFeedItemsRepository.GAE_READ_FEED_ITEMS_REPOSITORY;

/**
 * @author : igu
 */
public final class GaeRepositories {

    private static final int MAX_CACHED_FEED_UPDATE_TASK_REPOSITORY_BEFORE_FLUSH = 300;

    public static final FeedUpdateTaskSchedulerContextRepository GAE_FEED_UPDATE_TASK_SCHEDULER_CONTEXT_REPOSITORY = new CachedFeedUpdateTaskSchedulerContextRepository(MEM_CACHE);
    public static final FeedItemsRepository GAE_CACHED_FEED_ITEMS_REPOSITORY = new CachedFeedItemsRepository(GAE_FEED_ITEMS_REPOSITORY, MEM_CACHE);
    public static final FeedUpdateTaskRepository GAE_CACHED_FEED_UPDATE_TASK_REPOSITORY = new CachedFeedUpdateTaskRepository(GAE_FEED_UPDATE_TASK_REPOSITORY, MAX_CACHED_FEED_UPDATE_TASK_REPOSITORY_BEFORE_FLUSH, MEM_CACHE);
    public static final FeedHeadersRepository GAE_CACHED_FEED_HEADERS_REPOSITORY = new CachedFeedHeadersRepository(GAE_FEED_HEADERS_REPOSITORY, MEM_CACHE);
    public static final ReadFeedItemsRepository GAE_CACHED_READ_FEED_ITEMS_REPOSITORY = new CachedReadFeedItemsRepository(GAE_READ_FEED_ITEMS_REPOSITORY, MEM_CACHE);
    public static final CategoriesRepository GAE_CACHED_CATEGORIES_REPOSITORY = new CachedCategoriesRepository(GAE_CATEGORIES_REPOSITORY, MEM_CACHE);

    private GaeRepositories() {
        // empty
    }

}
