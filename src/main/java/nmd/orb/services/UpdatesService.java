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
import nmd.orb.services.reports.FeedSeriesUpdateReport;
import nmd.orb.services.reports.FeedUpdateReport;

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

    public UpdatesService(final FeedHeadersRepository feedHeadersRepository, final FeedItemsRepository feedItemsRepository, final FeedUpdateTaskRepository feedUpdateTaskRepository, final FeedUpdateTaskScheduler scheduler, final UrlFetcher fetcher, final Transactions transactions) {
        super(feedHeadersRepository, feedItemsRepository, fetcher);

        guard(notNull(transactions));
        this.transactions = transactions;

        guard(notNull(feedUpdateTaskRepository));
        this.feedUpdateTaskRepository = feedUpdateTaskRepository;

        guard(notNull(scheduler));
        this.scheduler = scheduler;
    }

    public FeedUpdateReport updateFeed(final UUID feedId) throws ServiceException {
        guard(isValidFeedHeaderId(feedId));

        Transaction getFeedHeaderAndTaskTransaction = null;

        final FeedHeader header;
        final FeedUpdateTask updateTask;

        try {
            getFeedHeaderAndTaskTransaction = this.transactions.beginOne();

            header = loadFeedHeader(feedId);

            updateTask = this.feedUpdateTaskRepository.loadTaskForFeedId(feedId);

            if (updateTask == null) {
                throw new ServiceException(wrongFeedTaskId(feedId));
            }

            getFeedHeaderAndTaskTransaction.commit();
        } finally {
            rollbackIfActive(getFeedHeaderAndTaskTransaction);
        }

        final Feed feed = fetchFeed(header.feedLink);

        Transaction updateFeedTransaction = null;

        try {
            updateFeedTransaction = this.transactions.beginOne();

            List<FeedItem> olds = getFeedOldItems(header);

            final FeedItemsMergeReport mergeReport = FeedItemsMerger.merge(olds, feed.items, updateTask.maxFeedItemsCount);
            final List<FeedItem> addedAndRetained = mergeReport.getAddedAndRetained();

            final boolean notTheSame = !FeedItemsMerger.listEqualsIgnoringGuid(olds, addedAndRetained);

            final FeedUpdateTask updatedTask = updateTask.updateStatistic(mergeReport.added.size());
            this.feedUpdateTaskRepository.updateTask(updatedTask);

            if (notTheSame) {
                this.feedItemsRepository.storeItems(header.id, addedAndRetained);
            } else {
                LOGGER.info(format("Feed id [ %s ] url [ %s ]. There are no changes. Nothing to store", header.id, header.feedLink));
            }

            updateFeedTransaction.commit();

            return new FeedUpdateReport(header.feedLink, feedId, mergeReport);
        } finally {
            rollbackIfActive(updateFeedTransaction);
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

}
