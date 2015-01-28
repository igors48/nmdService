package nmd.orb.gae;

import nmd.orb.collector.scheduler.CycleFeedUpdateTaskScheduler;
import nmd.orb.collector.scheduler.FeedUpdateTaskScheduler;
import nmd.orb.gae.repositories.GaeChangeRepository;
import nmd.orb.gae.repositories.GaeImportJobContextRepository;
import nmd.orb.services.*;

import static nmd.orb.gae.fetcher.GaeUrlFetcher.GAE_URL_FETCHER;

/**
 * @author : igu
 */
public final class GaeServices {

    public static final ChangeRegistrationService CHANGE_REGISTRATION_SERVICE =
            new ChangeRegistrationService(GaeChangeRepository.INSTANCE);

    public static final FeedUpdateTaskScheduler FEED_UPDATE_TASK_SCHEDULER =
            new CycleFeedUpdateTaskScheduler(GaeRepositories.INSTANCE.getGaeFeedUpdateTaskSchedulerContextRepository(),
                    GaeRepositories.INSTANCE.getGaeCachedFeedUpdateTaskRepository(),
                    GaeTransactions.INSTANCE);

    public static final CategoriesService CATEGORIES_SERVICE =
            new CategoriesService(GaeRepositories.INSTANCE.getGaeCachedCategoriesRepository(),
                    GaeRepositories.INSTANCE.getGaeCachedReadFeedItemsRepository(),
                    GaeRepositories.INSTANCE.getGaeCachedFeedHeadersRepository(),
                    GaeRepositories.INSTANCE.getGaeCachedFeedItemsRepository(),
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
            new ReadsService(GaeRepositories.INSTANCE.getGaeCachedFeedHeadersRepository(),
                    GaeRepositories.INSTANCE.getGaeCachedFeedItemsRepository(),
                    GaeRepositories.INSTANCE.getGaeCachedReadFeedItemsRepository(),
                    GAE_URL_FETCHER,
                    GaeTransactions.INSTANCE);

    public static final FeedsService FEEDS_SERVICE =
            new FeedsService(GaeRepositories.INSTANCE.getGaeCachedFeedHeadersRepository(),
                    GaeRepositories.INSTANCE.getGaeCachedFeedItemsRepository(),
                    GaeRepositories.INSTANCE.getGaeCachedFeedUpdateTaskRepository(),
                    GaeRepositories.INSTANCE.getGaeCachedReadFeedItemsRepository(),
                    GaeRepositories.INSTANCE.getGaeCachedCategoriesRepository(),
                    CHANGE_REGISTRATION_SERVICE,
                    GAE_URL_FETCHER,
                    GaeTransactions.INSTANCE);

    public static final UpdatesService UPDATES_SERVICE =
            new UpdatesService(GaeRepositories.INSTANCE.getGaeCachedFeedHeadersRepository(),
                    GaeRepositories.INSTANCE.getGaeCachedFeedItemsRepository(),
                    GaeRepositories.INSTANCE.getGaeCachedFeedUpdateTaskRepository(),
                    FEED_UPDATE_TASK_SCHEDULER,
                    GAE_URL_FETCHER,
                    GaeTransactions.INSTANCE);

    public static final ImportService IMPORT_SERVICE =
            new ImportService(GaeImportJobContextRepository.INSTANCE,
                    CATEGORIES_SERVICE,
                    FEEDS_SERVICE,
                    GaeTransactions.INSTANCE);

    public static final ResetService CLEAR_SERVICE =
            new ResetService(GaeRepositories.INSTANCE.getGaeCachedFeedHeadersRepository(),
                    GaeRepositories.INSTANCE.getGaeCachedFeedItemsRepository(),
                    GaeRepositories.INSTANCE.getGaeFeedUpdateTaskSchedulerContextRepository(),
                    GaeRepositories.INSTANCE.getGaeCachedFeedUpdateTaskRepository(),
                    GaeRepositories.INSTANCE.getGaeCachedReadFeedItemsRepository(),
                    GaeRepositories.INSTANCE.getGaeCachedCategoriesRepository(),
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
