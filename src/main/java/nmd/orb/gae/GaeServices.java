package nmd.orb.gae;

import nmd.orb.collector.scheduler.CycleFeedUpdateTaskScheduler;
import nmd.orb.collector.scheduler.FeedUpdateTaskScheduler;
import nmd.orb.gae.repositories.GaeChangeRepository;
import nmd.orb.gae.repositories.GaeImportJobContextRepository;
import nmd.orb.services.*;

import static nmd.orb.gae.GaeRepositories.*;
import static nmd.orb.gae.fetcher.GaeUrlFetcher.GAE_URL_FETCHER;

/**
 * @author : igu
 */
public final class GaeServices {

    public static final ChangeRegistrationService CHANGE_REGISTRATION_SERVICE =
            new ChangeRegistrationService(GaeChangeRepository.INSTANCE);

    public static final FeedUpdateTaskScheduler FEED_UPDATE_TASK_SCHEDULER =
            new CycleFeedUpdateTaskScheduler(GAE_FEED_UPDATE_TASK_SCHEDULER_CONTEXT_REPOSITORY,
                    GAE_CACHED_FEED_UPDATE_TASK_REPOSITORY,
                    GaeTransactions.INSTANCE);

    public static final CategoriesService CATEGORIES_SERVICE =
            new CategoriesService(GAE_CACHED_CATEGORIES_REPOSITORY,
                    GAE_CACHED_READ_FEED_ITEMS_REPOSITORY,
                    GAE_CACHED_FEED_HEADERS_REPOSITORY,
                    GAE_CACHED_FEED_ITEMS_REPOSITORY,
                    CHANGE_REGISTRATION_SERVICE,
                    GaeTransactions.INSTANCE);

    public static final MailService MAIL_SERVICE = new MailService();

    public static final AutoExportService AUTO_EXPORT_SERVICE =
            new AutoExportService(GaeChangeRepository.INSTANCE,
                    CATEGORIES_SERVICE,
                    MAIL_SERVICE,
                    GaeTransactions.INSTANCE
            );

    public static final ReadsService READS_SERVICE =
            new ReadsService(GAE_CACHED_FEED_HEADERS_REPOSITORY,
                    GAE_CACHED_FEED_ITEMS_REPOSITORY,
                    GAE_CACHED_READ_FEED_ITEMS_REPOSITORY,
                    GAE_URL_FETCHER,
                    GaeTransactions.INSTANCE);

    public static final FeedsService FEEDS_SERVICE =
            new FeedsService(GAE_CACHED_FEED_HEADERS_REPOSITORY,
                    GAE_CACHED_FEED_ITEMS_REPOSITORY,
                    GAE_CACHED_FEED_UPDATE_TASK_REPOSITORY,
                    GAE_CACHED_READ_FEED_ITEMS_REPOSITORY,
                    GAE_CACHED_CATEGORIES_REPOSITORY,
                    CHANGE_REGISTRATION_SERVICE,
                    GAE_URL_FETCHER,
                    GaeTransactions.INSTANCE);

    public static final UpdatesService UPDATES_SERVICE =
            new UpdatesService(GAE_CACHED_FEED_HEADERS_REPOSITORY,
                    GAE_CACHED_FEED_ITEMS_REPOSITORY,
                    GAE_CACHED_FEED_UPDATE_TASK_REPOSITORY,
                    FEED_UPDATE_TASK_SCHEDULER,
                    GAE_URL_FETCHER,
                    GaeTransactions.INSTANCE);

    public static final ImportService IMPORT_SERVICE =
            new ImportService(GaeImportJobContextRepository.INSTANCE,
                    CATEGORIES_SERVICE,
                    FEEDS_SERVICE,
                    GaeTransactions.INSTANCE);

    public static final ResetService CLEAR_SERVICE =
            new ResetService(GAE_CACHED_FEED_HEADERS_REPOSITORY,
                    GAE_CACHED_FEED_ITEMS_REPOSITORY,
                    GAE_FEED_UPDATE_TASK_SCHEDULER_CONTEXT_REPOSITORY,
                    GAE_CACHED_FEED_UPDATE_TASK_REPOSITORY,
                    GAE_CACHED_READ_FEED_ITEMS_REPOSITORY,
                    GAE_CACHED_CATEGORIES_REPOSITORY,
                    GaeImportJobContextRepository.INSTANCE,
                    GaeChangeRepository.INSTANCE,
                    CHANGE_REGISTRATION_SERVICE,
                    GaeTransactions.INSTANCE
            );

    public static final CronService CRON_SERVICE =
            new CronService(
                    UPDATES_SERVICE,
                    IMPORT_SERVICE,
                    AUTO_EXPORT_SERVICE
            );

    private GaeServices() {
        // empty
    }

}
