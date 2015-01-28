package nmd.orb.gae;

import nmd.orb.gae.cache.MemCache;
import nmd.orb.gae.repositories.GaeFeedItemsRepository;
import nmd.orb.repositories.*;
import nmd.orb.repositories.cached.*;

import static nmd.orb.gae.repositories.GaeCategoriesRepository.GAE_CATEGORIES_REPOSITORY;
import static nmd.orb.gae.repositories.GaeFeedHeadersRepository.GAE_FEED_HEADERS_REPOSITORY;
import static nmd.orb.gae.repositories.GaeFeedUpdateTaskRepository.GAE_FEED_UPDATE_TASK_REPOSITORY;
import static nmd.orb.gae.repositories.GaeReadFeedItemsRepository.GAE_READ_FEED_ITEMS_REPOSITORY;

/**
 * @author : igu
 */
public final class GaeRepositories {

    public static final FeedUpdateTaskSchedulerContextRepository GAE_FEED_UPDATE_TASK_SCHEDULER_CONTEXT_REPOSITORY = new CachedFeedUpdateTaskSchedulerContextRepository(MemCache.INSTANCE);
    public static final FeedItemsRepository GAE_CACHED_FEED_ITEMS_REPOSITORY = new CachedFeedItemsRepository(GaeFeedItemsRepository.INSTANCE, MemCache.INSTANCE);
    public static final FeedUpdateTaskRepository GAE_CACHED_FEED_UPDATE_TASK_REPOSITORY = new CachedFeedUpdateTaskRepository(GAE_FEED_UPDATE_TASK_REPOSITORY, MemCache.INSTANCE);
    public static final FeedHeadersRepository GAE_CACHED_FEED_HEADERS_REPOSITORY = new CachedFeedHeadersRepository(GAE_FEED_HEADERS_REPOSITORY, MemCache.INSTANCE);
    public static final ReadFeedItemsRepository GAE_CACHED_READ_FEED_ITEMS_REPOSITORY = new CachedReadFeedItemsRepository(GAE_READ_FEED_ITEMS_REPOSITORY, MemCache.INSTANCE);
    public static final CategoriesRepository GAE_CACHED_CATEGORIES_REPOSITORY = new CachedCategoriesRepository(GAE_CATEGORIES_REPOSITORY, MemCache.INSTANCE);

    private GaeRepositories() {
        // empty
    }

}
