package nmd.orb.gae;

import nmd.orb.gae.cache.MemCache;
import nmd.orb.gae.repositories.*;
import nmd.orb.repositories.*;
import nmd.orb.repositories.cached.*;

/**
 * @author : igu
 */
public enum GaeRepositories {

    INSTANCE;

    private final FeedUpdateTaskSchedulerContextRepository gaeFeedUpdateTaskSchedulerContextRepository = new CachedFeedUpdateTaskSchedulerContextRepository(MemCache.INSTANCE);
    private final FeedItemsRepository gaeCachedFeedItemsRepository = new CachedFeedItemsRepository(GaeFeedItemsRepository.INSTANCE, MemCache.INSTANCE);
    private final FeedUpdateTaskRepository gaeCachedFeedUpdateTaskRepository = new CachedFeedUpdateTaskRepository(GaeFeedUpdateTaskRepository.INSTANCE, MemCache.INSTANCE);
    private final FeedHeadersRepository gaeCachedFeedHeadersRepository = new CachedFeedHeadersRepository(GaeFeedHeadersRepository.INSTANCE, MemCache.INSTANCE);
    private final ReadFeedItemsRepository gaeCachedReadFeedItemsRepository = new CachedReadFeedItemsRepository(GaeReadFeedItemsRepository.INSTANCE, MemCache.INSTANCE);
    private final CategoriesRepository gaeCachedCategoriesRepository = new CachedCategoriesRepository(GaeCategoriesRepository.INSTANCE, MemCache.INSTANCE);

    public FeedUpdateTaskSchedulerContextRepository getGaeFeedUpdateTaskSchedulerContextRepository() {
        return gaeFeedUpdateTaskSchedulerContextRepository;
    }

    public FeedItemsRepository getGaeCachedFeedItemsRepository() {
        return gaeCachedFeedItemsRepository;
    }

    public FeedUpdateTaskRepository getGaeCachedFeedUpdateTaskRepository() {
        return gaeCachedFeedUpdateTaskRepository;
    }

    public FeedHeadersRepository getGaeCachedFeedHeadersRepository() {
        return gaeCachedFeedHeadersRepository;
    }

    public ReadFeedItemsRepository getGaeCachedReadFeedItemsRepository() {
        return gaeCachedReadFeedItemsRepository;
    }

    public CategoriesRepository getGaeCachedCategoriesRepository() {
        return gaeCachedCategoriesRepository;
    }

}
