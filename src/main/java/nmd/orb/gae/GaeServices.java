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
public enum GaeServices {

    INSTANCE;

    private final ChangeRegistrationService changeRegistrationService =
            new ChangeRegistrationService(GaeChangeRepository.INSTANCE);

    private final FeedUpdateTaskScheduler feedUpdateTaskScheduler =
            new CycleFeedUpdateTaskScheduler(GaeRepositories.INSTANCE.getFeedUpdateTaskSchedulerContextRepository(),
                    GaeRepositories.INSTANCE.getFeedUpdateTaskRepository(),
                    GaeTransactions.INSTANCE);

    private final CategoriesService categoriesService =
            new CategoriesService(GaeRepositories.INSTANCE.getCategoriesRepository(),
                    GaeRepositories.INSTANCE.getReadFeedItemsRepository(),
                    GaeRepositories.INSTANCE.getFeedHeadersRepository(),
                    GaeRepositories.INSTANCE.getFeedItemsRepository(),
                    changeRegistrationService,
                    GaeTransactions.INSTANCE);

    private final MailService mailService = new MailService();

    private final AutoExportService autoExportService =
            new AutoExportService(GaeChangeRepository.INSTANCE,
                    categoriesService,
                    mailService,
                    GaeTransactions.INSTANCE
            );

    private final ReadsService readsService =
            new ReadsService(GaeRepositories.INSTANCE.getFeedHeadersRepository(),
                    GaeRepositories.INSTANCE.getFeedItemsRepository(),
                    GaeRepositories.INSTANCE.getReadFeedItemsRepository(),
                    GAE_URL_FETCHER,
                    GaeTransactions.INSTANCE);

    private final FeedsService feedsService =
            new FeedsService(GaeRepositories.INSTANCE.getFeedHeadersRepository(),
                    GaeRepositories.INSTANCE.getFeedItemsRepository(),
                    GaeRepositories.INSTANCE.getFeedUpdateTaskRepository(),
                    GaeRepositories.INSTANCE.getReadFeedItemsRepository(),
                    GaeRepositories.INSTANCE.getCategoriesRepository(),
                    changeRegistrationService,
                    GAE_URL_FETCHER,
                    GaeTransactions.INSTANCE);

    private final UpdatesService updatesService =
            new UpdatesService(GaeRepositories.INSTANCE.getFeedHeadersRepository(),
                    GaeRepositories.INSTANCE.getFeedItemsRepository(),
                    GaeRepositories.INSTANCE.getFeedUpdateTaskRepository(),
                    feedUpdateTaskScheduler,
                    GAE_URL_FETCHER,
                    GaeTransactions.INSTANCE);

    private final ImportService importService =
            new ImportService(GaeImportJobContextRepository.INSTANCE,
                    categoriesService,
                    feedsService,
                    GaeTransactions.INSTANCE);

    private final ResetService resetService =
            new ResetService(GaeRepositories.INSTANCE.getFeedHeadersRepository(),
                    GaeRepositories.INSTANCE.getFeedItemsRepository(),
                    GaeRepositories.INSTANCE.getFeedUpdateTaskSchedulerContextRepository(),
                    GaeRepositories.INSTANCE.getFeedUpdateTaskRepository(),
                    GaeRepositories.INSTANCE.getReadFeedItemsRepository(),
                    GaeRepositories.INSTANCE.getCategoriesRepository(),
                    GaeImportJobContextRepository.INSTANCE,
                    GaeChangeRepository.INSTANCE,
                    changeRegistrationService,
                    GaeTransactions.INSTANCE
            );

    private final CronService cronService =
            new CronService(
                    updatesService,
                    importService,
                    autoExportService
            );

    private final ContentFilterService contentFilterService =
            new ContentFilterService(
                    GAE_URL_FETCHER
            );

    public CategoriesService getCategoriesService() {
        return this.categoriesService;
    }

    public ReadsService getReadsService() {
        return this.readsService;
    }

    public FeedsService getFeedsService() {
        return this.feedsService;
    }

    public UpdatesService getUpdatesService() {
        return this.updatesService;
    }

    public ImportService getImportService() {
        return this.importService;
    }

    public ResetService getResetService() {
        return this.resetService;
    }

    public CronService getCronService() {
        return this.cronService;
    }

    public ContentFilterService getContentFilterService() {
        return this.contentFilterService;
    }

}
