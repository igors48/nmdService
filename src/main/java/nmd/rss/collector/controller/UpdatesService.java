package nmd.rss.collector.controller;

import com.google.appengine.api.datastore.Transaction;
import nmd.rss.collector.Transactions;
import nmd.rss.collector.error.ServiceError;
import nmd.rss.collector.error.ServiceException;
import nmd.rss.collector.feed.*;
import nmd.rss.collector.scheduler.FeedUpdateTask;
import nmd.rss.collector.scheduler.FeedUpdateTaskRepository;
import nmd.rss.collector.scheduler.FeedUpdateTaskScheduler;
import nmd.rss.collector.updater.FeedHeadersRepository;
import nmd.rss.collector.updater.FeedItemsRepository;
import nmd.rss.collector.updater.UrlFetcher;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.format;
import static nmd.rss.collector.error.ServiceError.wrongFeedTaskId;
import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.collector.util.TransactionTools.rollbackIfActive;

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

        assertNotNull(transactions);
        this.transactions = transactions;

        assertNotNull(feedUpdateTaskRepository);
        this.feedUpdateTaskRepository = feedUpdateTaskRepository;

        assertNotNull(scheduler);
        this.scheduler = scheduler;
    }

    public FeedUpdateReport updateFeed(final UUID feedId) throws ServiceException {
        assertNotNull(feedId);

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
            final boolean nothingChanged = mergeReport.added.isEmpty() && mergeReport.removed.isEmpty();

            if (!nothingChanged) {
                this.feedItemsRepository.storeItems(header.id, mergeReport.getAddedAndRetained());
            }

            updateFeedTransaction.commit();

            return new FeedUpdateReport(header.feedLink, feedId, mergeReport);
        } finally {
            rollbackIfActive(updateFeedTransaction);
        }
    }

    public FeedSeriesUpdateReport updateCurrentFeeds(final Quota quota) {
        assertNotNull(quota);

        final List<FeedUpdateReport> updateReports = new ArrayList<>();
        final List<ServiceError> errors = new ArrayList<>();

        int count = 0;

        final Set<FeedUpdateTask> updated = new HashSet<>();

        while (!quota.expired()) {
            final FeedUpdateTask currentTask = this.scheduler.getCurrentTask();

            if (updated.contains(currentTask)) {
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

            updated.add(currentTask);
            ++count;
        }

        LOGGER.info(format("[ %d ] feeds were updated", count));

        return new FeedSeriesUpdateReport(updateReports, errors);
    }

}
