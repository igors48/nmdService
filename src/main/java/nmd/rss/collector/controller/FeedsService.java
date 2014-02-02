package nmd.rss.collector.controller;

import com.google.appengine.api.datastore.Transaction;
import nmd.rss.collector.Transactions;
import nmd.rss.collector.error.ServiceException;
import nmd.rss.collector.feed.*;
import nmd.rss.collector.scheduler.FeedUpdateTask;
import nmd.rss.collector.scheduler.FeedUpdateTaskRepository;
import nmd.rss.collector.scheduler.FeedUpdateTaskScheduler;
import nmd.rss.collector.scheduler.FeedUpdateTaskSchedulerContextRepository;
import nmd.rss.collector.updater.FeedHeadersRepository;
import nmd.rss.collector.updater.FeedItemsRepository;
import nmd.rss.collector.updater.UrlFetcher;
import nmd.rss.reader.ReadFeedItemsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.collector.util.Assert.assertStringIsValid;
import static nmd.rss.collector.util.TransactionTools.rollbackIfActive;
import static nmd.rss.collector.util.UrlTools.normalizeUrl;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 02.02.14
 */
public class FeedsService extends AbstractService {

    public FeedsService(final FeedHeadersRepository feedHeadersRepository, final FeedItemsRepository feedItemsRepository, final FeedUpdateTaskRepository feedUpdateTaskRepository, final ReadFeedItemsRepository readFeedItemsRepository, final FeedUpdateTaskSchedulerContextRepository feedUpdateTaskSchedulerContextRepository, final FeedUpdateTaskScheduler scheduler, final UrlFetcher fetcher, final Transactions transactions) {
        super(feedHeadersRepository, feedItemsRepository, feedUpdateTaskRepository, readFeedItemsRepository, feedUpdateTaskSchedulerContextRepository, scheduler, fetcher, transactions);
    }

    public UUID addFeed(final String feedUrl) throws ServiceException {
        assertStringIsValid(feedUrl);

        Transaction transaction = null;

        final String feedUrlInLowerCase = normalizeUrl(feedUrl);
        final Feed feed = fetchFeed(feedUrlInLowerCase);

        try {
            transaction = this.transactions.beginOne();

            FeedHeader feedHeader = this.feedHeadersRepository.loadHeader(feedUrlInLowerCase);

            if (feedHeader == null) {
                feedHeader = feed.header;
                this.feedHeadersRepository.storeHeader(feedHeader);
            }

            final List<FeedItem> olds = getFeedOldItems(feedHeader);

            createFeedUpdateTask(feedHeader);

            final FeedItemsMergeReport mergeReport = FeedItemsMerger.merge(olds, feed.items, MAX_FEED_ITEMS_COUNT);
            this.feedItemsRepository.mergeItems(feedHeader.id, mergeReport);

            transaction.commit();

            return feedHeader.id;
        } finally {
            rollbackIfActive(transaction);
        }
    }

    public void updateFeedTitle(final UUID feedId, final String title) throws ServiceException {
        assertNotNull(feedId);
        assertStringIsValid(title);

        Transaction transaction = null;

        try {
            transaction = this.transactions.beginOne();

            final FeedHeader oldHeader = loadFeedHeader(feedId);
            final FeedHeader newHeader = oldHeader.changeTitle(title);

            this.feedHeadersRepository.deleteHeader(oldHeader.id);
            this.feedHeadersRepository.storeHeader(newHeader);

            transaction.commit();
        } finally {
            rollbackIfActive(transaction);
        }
    }

    public void removeFeed(final UUID feedId) {
        assertNotNull(feedId);

        Transaction transaction = null;

        try {
            transaction = this.transactions.beginOne();

            this.feedUpdateTaskRepository.deleteTaskForFeedId(feedId);
            this.feedHeadersRepository.deleteHeader(feedId);
            this.feedItemsRepository.deleteItems(feedId);
            this.readFeedItemsRepository.delete(feedId);

            transaction.commit();
        } finally {
            rollbackIfActive(transaction);
        }
    }

    public List<FeedHeader> getFeedHeaders() {
        Transaction transaction = null;

        try {
            transaction = this.transactions.beginOne();

            final List<FeedHeader> headers = this.feedHeadersRepository.loadHeaders();

            transaction.commit();

            return headers;
        } finally {
            rollbackIfActive(transaction);
        }
    }

    public Feed getFeed(final UUID feedId) throws ServiceException {
        assertNotNull(feedId);

        Transaction transaction = null;

        try {
            transaction = this.transactions.beginOne();

            final FeedHeader header = loadFeedHeader(feedId);

            List<FeedItem> items = this.feedItemsRepository.loadItems(feedId);
            items = items == null ? new ArrayList<FeedItem>() : items;

            transaction.commit();

            return new Feed(header, items);
        } finally {
            rollbackIfActive(transaction);
        }
    }

    public void clear() {

        Transaction transaction = null;

        try {
            transaction = this.transactions.beginOne();

            final List<FeedHeader> headers = this.feedHeadersRepository.loadHeaders();
            final List<FeedHeader> backedHeaders = new ArrayList<>(headers);

            for (final FeedHeader header : backedHeaders) {
                removeFeed(header.id);
            }

            this.feedUpdateTaskSchedulerContextRepository.clear();

            transaction.commit();
        } finally {
            rollbackIfActive(transaction);
        }
    }

    private void createFeedUpdateTask(final FeedHeader feedHeader) {
        FeedUpdateTask feedUpdateTask = this.feedUpdateTaskRepository.loadTaskForFeedId(feedHeader.id);

        if (feedUpdateTask == null) {
            feedUpdateTask = new FeedUpdateTask(feedHeader.id, MAX_FEED_ITEMS_COUNT);
            this.feedUpdateTaskRepository.storeTask(feedUpdateTask);
        }
    }

}
