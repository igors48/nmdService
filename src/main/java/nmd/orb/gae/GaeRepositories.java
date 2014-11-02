package nmd.orb.gae;

import nmd.orb.repositories.*;
import nmd.orb.repositories.cached.*;

import static nmd.orb.gae.cache.MemCache.MEM_CACHE;
import static nmd.orb.gae.repositories.GaeCategoriesRepository.GAE_CATEGORIES_REPOSITORY;
import static nmd.orb.gae.repositories.GaeFeedHeadersRepository.GAE_FEED_HEADERS_REPOSITORY;
import static nmd.orb.gae.repositories.GaeFeedItemsRepository.GAE_FEED_ITEMS_REPOSITORY;
import static nmd.orb.gae.repositories.GaeFeedUpdateTaskRepository.GAE_FEED_UPDATE_TASK_REPOSITORY;
import static nmd.orb.gae.repositories.GaeReadFeedItemsRepository.GAE_READ_FEED_ITEMS_REPOSITORY;

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
