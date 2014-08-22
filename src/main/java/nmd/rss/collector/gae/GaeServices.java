package nmd.rss.collector.gae;

import nmd.rss.collector.controller.CategoriesService;
import nmd.rss.collector.controller.FeedsService;
import nmd.rss.collector.controller.ReadsService;
import nmd.rss.collector.controller.UpdatesService;
import nmd.rss.collector.scheduler.CycleFeedUpdateTaskScheduler;
import nmd.rss.collector.scheduler.FeedUpdateTaskScheduler;

import static nmd.rss.collector.gae.GaeRepositories.*;
import static nmd.rss.collector.gae.GaeTransactions.GAE_TRANSACTIONS;
import static nmd.rss.collector.gae.fetcher.GaeUrlFetcher.GAE_URL_FETCHER;

/**
 * @author : igu
 */
public final class GaeServices {

    public static final FeedUpdateTaskScheduler GAE_FEED_UPDATE_TASK_SCHEDULER =
            new CycleFeedUpdateTaskScheduler(GAE_FEED_UPDATE_TASK_SCHEDULER_CONTEXT_REPOSITORY,
                    GAE_CACHED_FEED_UPDATE_TASK_REPOSITORY,
                    GAE_TRANSACTIONS);

    public static final CategoriesService CATEGORIES_SERVICE =
            new CategoriesService(GAE_CACHED_CATEGORIES_REPOSITORY,
                    GAE_CACHED_READ_FEED_ITEMS_REPOSITORY,
                    GAE_CACHED_FEED_HEADERS_REPOSITORY,
                    GAE_CACHED_FEED_ITEMS_REPOSITORY,
                    GAE_TRANSACTIONS);

    public static final ReadsService READS_SERVICE =
            new ReadsService(GAE_CACHED_FEED_HEADERS_REPOSITORY,
                    GAE_CACHED_FEED_ITEMS_REPOSITORY,
                    GAE_CACHED_READ_FEED_ITEMS_REPOSITORY,
                    GAE_URL_FETCHER,
                    GAE_TRANSACTIONS);

    public static final FeedsService FEEDS_SERVICE =
            new FeedsService(GAE_CACHED_FEED_HEADERS_REPOSITORY,
                    GAE_CACHED_FEED_ITEMS_REPOSITORY,
                    GAE_CACHED_FEED_UPDATE_TASK_REPOSITORY,
                    GAE_CACHED_READ_FEED_ITEMS_REPOSITORY,
                    GAE_CACHED_CATEGORIES_REPOSITORY,
                    GAE_FEED_UPDATE_TASK_SCHEDULER_CONTEXT_REPOSITORY,
                    GAE_URL_FETCHER,
                    GAE_TRANSACTIONS);

    public static final UpdatesService UPDATES_SERVICE =
            new UpdatesService(GAE_CACHED_FEED_HEADERS_REPOSITORY,
                    GAE_CACHED_FEED_ITEMS_REPOSITORY,
                    GAE_CACHED_FEED_UPDATE_TASK_REPOSITORY,
                    GAE_FEED_UPDATE_TASK_SCHEDULER,
                    GAE_URL_FETCHER,
                    GAE_TRANSACTIONS);

    private GaeServices() {
        // empty
    }

}
