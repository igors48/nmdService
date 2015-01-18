package nmd.orb.gae;

import nmd.orb.collector.scheduler.CycleFeedUpdateTaskScheduler;
import nmd.orb.collector.scheduler.FeedUpdateTaskScheduler;
import nmd.orb.gae.repositories.GaeImportJobContextRepository;
import nmd.orb.services.*;

import static nmd.orb.gae.GaeRepositories.*;
import static nmd.orb.gae.GaeTransactions.GAE_TRANSACTIONS;
import static nmd.orb.gae.fetcher.GaeUrlFetcher.GAE_URL_FETCHER;
import static nmd.orb.gae.repositories.GaeImportJobContextRepository.GAE_IMPORT_JOB_CONTEXT_REPOSITORY;

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
                    new AutoExportService(),
                    GAE_URL_FETCHER,
                    GAE_TRANSACTIONS);

    public static final UpdatesService UPDATES_SERVICE =
            new UpdatesService(GAE_CACHED_FEED_HEADERS_REPOSITORY,
                    GAE_CACHED_FEED_ITEMS_REPOSITORY,
                    GAE_CACHED_FEED_UPDATE_TASK_REPOSITORY,
                    GAE_FEED_UPDATE_TASK_SCHEDULER,
                    GAE_URL_FETCHER,
                    GAE_TRANSACTIONS);

    public static final ImportService IMPORT_SERVICE =
            new ImportService(GAE_IMPORT_JOB_CONTEXT_REPOSITORY,
                    CATEGORIES_SERVICE,
                    FEEDS_SERVICE,
                    GAE_TRANSACTIONS);

    public static final ResetService CLEAR_SERVICE =
            new ResetService(GAE_CACHED_FEED_HEADERS_REPOSITORY,
                    GAE_CACHED_FEED_ITEMS_REPOSITORY,
                    GAE_FEED_UPDATE_TASK_SCHEDULER_CONTEXT_REPOSITORY,
                    GAE_CACHED_FEED_UPDATE_TASK_REPOSITORY,
                    GAE_CACHED_READ_FEED_ITEMS_REPOSITORY,
                    GAE_CACHED_CATEGORIES_REPOSITORY,
                    GaeImportJobContextRepository.GAE_IMPORT_JOB_CONTEXT_REPOSITORY,
                    GAE_TRANSACTIONS
            );

    public static final CronService CRON_SERVICE =
            new CronService(
                    UPDATES_SERVICE,
                    IMPORT_SERVICE
            );

    private GaeServices() {
        // empty
    }

}
