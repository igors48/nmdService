package nmd.rss.collector.controller;

import com.google.appengine.api.datastore.Transaction;
import nmd.rss.collector.Transactions;
import nmd.rss.collector.error.ServiceException;
import nmd.rss.collector.feed.*;
import nmd.rss.collector.scheduler.FeedUpdateTask;
import nmd.rss.collector.scheduler.FeedUpdateTaskRepository;
import nmd.rss.collector.scheduler.FeedUpdateTaskScheduler;
import nmd.rss.collector.updater.FeedHeadersRepository;
import nmd.rss.collector.updater.FeedItemsRepository;
import nmd.rss.collector.updater.UrlFetcher;

import java.util.List;
import java.util.UUID;

import static nmd.rss.collector.error.ServiceError.noScheduledTask;
import static nmd.rss.collector.error.ServiceError.wrongFeedTaskId;
import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.collector.util.TransactionTools.rollbackIfActive;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 02.02.14
 */
public class UpdatesService extends AbstractService {

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
            this.feedItemsRepository.mergeItems(header.id, mergeReport);

            updateFeedTransaction.commit();

            return new FeedUpdateReport(header.feedLink, feedId, mergeReport);
        } finally {
            rollbackIfActive(updateFeedTransaction);
        }
    }

    public FeedUpdateReport updateCurrentFeed() throws ServiceException {
        final FeedUpdateTask currentTask = this.scheduler.getCurrentTask();

        if (currentTask == null) {
            throw new ServiceException(noScheduledTask());
        }

        return updateFeed(currentTask.feedId);
    }

}
