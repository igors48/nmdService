package nmd.orb.services;

import com.google.appengine.api.datastore.Transaction;
import nmd.orb.collector.fetcher.UrlFetcher;
import nmd.orb.collector.merger.FeedItemsMergeReport;
import nmd.orb.collector.merger.FeedItemsMerger;
import nmd.orb.collector.scheduler.FeedUpdateTask;
import nmd.orb.collector.scheduler.FeedUpdateTaskScheduler;
import nmd.orb.error.ServiceError;
import nmd.orb.error.ServiceException;
import nmd.orb.feed.Feed;
import nmd.orb.feed.FeedHeader;
import nmd.orb.feed.FeedItem;
import nmd.orb.repositories.FeedHeadersRepository;
import nmd.orb.repositories.FeedItemsRepository;
import nmd.orb.repositories.FeedUpdateTaskRepository;
import nmd.orb.repositories.Transactions;
import nmd.orb.services.quota.Quota;
import nmd.orb.services.report.FeedSeriesUpdateReport;
import nmd.orb.services.report.FeedUpdateReport;
import nmd.orb.services.update.FeedLinkAndMaxFeedItemsCount;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.format;
import static nmd.orb.error.ServiceError.wrongFeedTaskId;
import static nmd.orb.feed.FeedHeader.isValidFeedHeaderId;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;
import static nmd.orb.util.TransactionTools.rollbackIfActive;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 02.02.14
 */
public class UpdatesService extends AbstractService {

    private static final Logger LOGGER = Logger.getLogger(UpdatesService.class.getName());

    private final Transactions transactions;
    private final FeedUpdateTaskRepository feedUpdateTaskRepository;
    private final FeedUpdateTaskScheduler scheduler;
    private final UpdateErrorRegistrationService updateErrorRegistrationService;

    public UpdatesService(final FeedHeadersRepository feedHeadersRepository, final FeedItemsRepository feedItemsRepository, final FeedUpdateTaskRepository feedUpdateTaskRepository, final FeedUpdateTaskScheduler scheduler, final UpdateErrorRegistrationService updateErrorRegistrationService, final UrlFetcher fetcher, final Transactions transactions) {
        super(feedHeadersRepository, feedItemsRepository, fetcher);

        guard(notNull(transactions));
        this.transactions = transactions;

        guard(notNull(feedUpdateTaskRepository));
        this.feedUpdateTaskRepository = feedUpdateTaskRepository;

        guard(notNull(scheduler));
        this.scheduler = scheduler;

        guard(notNull(updateErrorRegistrationService));
        this.updateErrorRegistrationService = updateErrorRegistrationService;
    }

    public FeedUpdateReport updateFeed(final UUID feedId) throws ServiceException {
        guard(isValidFeedHeaderId(feedId));

        try {
            final FeedLinkAndMaxFeedItemsCount feedLinkAndMaxFeedItemsCount = getFeedLinkAndMaxFeedItemsCount(feedId);

            final Feed feed = fetchFeed(feedLinkAndMaxFeedItemsCount.feedLink);

            final FeedUpdateReport feedUpdateReport = mergeFeed(feedId, feedLinkAndMaxFeedItemsCount.maxFeedItemsCount, feed);

            this.updateErrorRegistrationService.updateSuccess(feedId);

            return feedUpdateReport;
        } catch (ServiceException exception) {
            this.updateErrorRegistrationService.updateError(feedId);

            throw exception;
        }
    }

    public FeedSeriesUpdateReport updateCurrentFeeds(final Quota quota) {
        guard(notNull(quota));

        final List<FeedUpdateReport> updateReports = new ArrayList<>();
        final List<ServiceError> errors = new ArrayList<>();

        final Set<UUID> updated = new HashSet<>();

        while (!quota.expired()) {
            final FeedUpdateTask currentTask = this.scheduler.getCurrentTask();

            if (currentTask == null) {
                LOGGER.info("There is no feed for update");

                break;
            }

            if (updated.contains(currentTask.feedId)) {
                LOGGER.info(format("Feed [ %s ] was already updated in this series", currentTask.feedId));

                break;
            }

            try {
                final FeedUpdateReport report = updateFeed(currentTask.feedId);
                updateReports.add(report);

                LOGGER.info(format("A: [ %d ] R: [ %d ] D: [ %d ] Feed link [ %s ] id [ %s ] updated.", report.mergeReport.added.size(), report.mergeReport.retained.size(), report.mergeReport.removed.size(), report.feedLink, report.feedId));
            } catch (ServiceException exception) {
                final ServiceError serviceError = exception.getError();
                errors.add(serviceError);

                LOGGER.log(Level.SEVERE, format("Error update current feed [ %s ]", serviceError), exception);
            }

            updated.add(currentTask.feedId);
        }

        LOGGER.info(format("[ %d ] feeds were updated", updated.size()));

        return new FeedSeriesUpdateReport(updateReports, errors);
    }

    private FeedLinkAndMaxFeedItemsCount getFeedLinkAndMaxFeedItemsCount(final UUID feedId) throws ServiceException {
        Transaction getFeedHeaderAndTaskTransaction = null;

        final FeedLinkAndMaxFeedItemsCount feedLinkAndMaxFeedItemsCount;

        try {
            getFeedHeaderAndTaskTransaction = this.transactions.beginOne();

            final FeedUpdateTask updateTask = this.feedUpdateTaskRepository.loadTaskForFeedId(feedId);

            if (updateTask == null) {
                throw new ServiceException(wrongFeedTaskId(feedId));
            }

            getFeedHeaderAndTaskTransaction.commit();

            feedLinkAndMaxFeedItemsCount = new FeedLinkAndMaxFeedItemsCount(loadFeedHeader(feedId).feedLink, updateTask.maxFeedItemsCount);
        } finally {
            rollbackIfActive(getFeedHeaderAndTaskTransaction);
        }
        return feedLinkAndMaxFeedItemsCount;
    }

    private FeedUpdateReport mergeFeed(final UUID feedId, final int maxFeedItemsCount, final Feed fetchedFeed) throws ServiceException {
        Transaction updateFeedTransaction = null;

        try {
            updateFeedTransaction = this.transactions.beginOne();

            final FeedHeader header = loadFeedHeader(feedId);

            List<FeedItem> olds = getFeedOldItems(header);

            final FeedItemsMergeReport mergeReport = FeedItemsMerger.merge(olds, fetchedFeed.items, maxFeedItemsCount);
            final List<FeedItem> addedAndRetained = mergeReport.getAddedAndRetained();

            final boolean needsToStore = !(mergeReport.added.isEmpty() && mergeReport.removed.isEmpty());

            if (needsToStore) {
                this.feedItemsRepository.storeItems(header.id, addedAndRetained);
            }

            updateFeedTransaction.commit();

            return new FeedUpdateReport(header.feedLink, feedId, mergeReport);
        } finally {
            rollbackIfActive(updateFeedTransaction);
        }
    }

}
