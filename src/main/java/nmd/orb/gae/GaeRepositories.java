package nmd.orb.gae;

import nmd.orb.gae.cache.MemCache;
import nmd.orb.gae.repositories.*;
import nmd.orb.repositories.*;
import nmd.orb.repositories.cached.*;

/**
 * @author : igu
 */
public final class GaeRepositories {

    public static final FeedUpdateTaskSchedulerContextRepository GAE_FEED_UPDATE_TASK_SCHEDULER_CONTEXT_REPOSITORY = new CachedFeedUpdateTaskSchedulerContextRepository(MemCache.INSTANCE);
    public static final FeedItemsRepository GAE_CACHED_FEED_ITEMS_REPOSITORY = new CachedFeedItemsRepository(GaeFeedItemsRepository.INSTANCE, MemCache.INSTANCE);
    public static final FeedUpdateTaskRepository GAE_CACHED_FEED_UPDATE_TASK_REPOSITORY = new CachedFeedUpdateTaskRepository(GaeFeedUpdateTaskRepository.INSTANCE, MemCache.INSTANCE);
    public static final FeedHeadersRepository GAE_CACHED_FEED_HEADERS_REPOSITORY = new CachedFeedHeadersRepository(GaeFeedHeadersRepository.INSTANCE, MemCache.INSTANCE);
    public static final ReadFeedItemsRepository GAE_CACHED_READ_FEED_ITEMS_REPOSITORY = new CachedReadFeedItemsRepository(GaeReadFeedItemsRepository.INSTANCE, MemCache.INSTANCE);
    public static final CategoriesRepository GAE_CACHED_CATEGORIES_REPOSITORY = new CachedCategoriesRepository(GaeCategoriesRepository.INSTANCE, MemCache.INSTANCE);

    private GaeRepositories() {
        // empty
    }

}
