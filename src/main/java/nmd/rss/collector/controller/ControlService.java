package nmd.rss.collector.controller;

import com.google.appengine.api.datastore.Transaction;
import nmd.rss.collector.Transactions;
import nmd.rss.collector.feed.*;
import nmd.rss.collector.scheduler.FeedUpdateTask;
import nmd.rss.collector.scheduler.FeedUpdateTaskRepository;
import nmd.rss.collector.scheduler.FeedUpdateTaskScheduler;
import nmd.rss.collector.updater.FeedHeadersRepository;
import nmd.rss.collector.updater.FeedItemsRepository;
import nmd.rss.collector.updater.UrlFetcher;
import nmd.rss.collector.updater.UrlFetcherException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static nmd.rss.collector.error.ServiceError.*;
import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.collector.util.Assert.assertStringIsValid;
import static nmd.rss.collector.util.TransactionTools.rollbackIfActive;
import static nmd.rss.collector.util.UrlTools.normalizeUrl;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.05.13
 */
public class ControlService {

    private static final int MAX_FEED_ITEMS_COUNT = 10000;

    private final Transactions transactions;

    private final FeedHeadersRepository feedHeadersRepository;
    private final FeedItemsRepository feedItemsRepository;
    private final FeedUpdateTaskRepository feedUpdateTaskRepository;

    private final FeedUpdateTaskScheduler scheduler;
    private final UrlFetcher fetcher;

    public ControlService(final FeedHeadersRepository feedHeadersRepository, final FeedItemsRepository feedItemsRepository, final FeedUpdateTaskRepository feedUpdateTaskRepository, final FeedUpdateTaskScheduler scheduler, final UrlFetcher fetcher, final Transactions transactions) {
        assertNotNull(feedHeadersRepository);
        this.feedHeadersRepository = feedHeadersRepository;

        assertNotNull(feedItemsRepository);
        this.feedItemsRepository = feedItemsRepository;

        assertNotNull(feedUpdateTaskRepository);
        this.feedUpdateTaskRepository = feedUpdateTaskRepository;

        assertNotNull(scheduler);
        this.scheduler = scheduler;

        assertNotNull(fetcher);
        this.fetcher = fetcher;

        assertNotNull(transactions);
        this.transactions = transactions;
    }

    public UUID addFeed(final String feedUrl) throws ControlServiceException {
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

    public void removeFeed(final UUID feedId) {
        assertNotNull(feedId);

        Transaction transaction = null;

        try {
            transaction = this.transactions.beginOne();

            this.feedUpdateTaskRepository.deleteTaskForFeedId(feedId);
            this.feedHeadersRepository.deleteHeader(feedId);
            this.feedItemsRepository.deleteItems(feedId);

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

    public Feed getFeed(final UUID feedId) throws ControlServiceException {
        assertNotNull(feedId);

        Transaction transaction = null;

        try {
            transaction = this.transactions.beginOne();

            final FeedHeader header = this.feedHeadersRepository.loadHeader(feedId);

            if (header == null) {
                throw new ControlServiceException(wrongFeedId(feedId));
            }

            List<FeedItem> items = this.feedItemsRepository.loadItems(feedId);
            items = items == null ? new ArrayList<FeedItem>() : items;

            transaction.commit();

            return new Feed(header, items);
        } finally {
            rollbackIfActive(transaction);
        }
    }

    public FeedUpdateReport updateFeed(final UUID feedId) throws ControlServiceException {
        assertNotNull(feedId);

        Transaction getFeedHeaderAndTaskTransaction = null;

        final FeedHeader header;
        final FeedUpdateTask updateTask;

        try {
            getFeedHeaderAndTaskTransaction = this.transactions.beginOne();

            header = this.feedHeadersRepository.loadHeader(feedId);

            if (header == null) {
                throw new ControlServiceException(wrongFeedId(feedId));
            }

            updateTask = this.feedUpdateTaskRepository.loadTaskForFeedId(feedId);

            if (updateTask == null) {
                throw new ControlServiceException(wrongFeedTaskId(feedId));
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

    public FeedUpdateReport updateCurrentFeed() throws ControlServiceException {
        final FeedUpdateTask currentTask = this.scheduler.getCurrentTask();

        if (currentTask == null) {
            throw new ControlServiceException(noScheduledTask());
        }

        return updateFeed(currentTask.feedId);
    }

    private void createFeedUpdateTask(final FeedHeader feedHeader) {
        FeedUpdateTask feedUpdateTask = this.feedUpdateTaskRepository.loadTaskForFeedId(feedHeader.id);

        if (feedUpdateTask == null) {
            feedUpdateTask = new FeedUpdateTask(feedHeader.id, MAX_FEED_ITEMS_COUNT);
            this.feedUpdateTaskRepository.storeTask(feedUpdateTask);
        }
    }

    private List<FeedItem> getFeedOldItems(final FeedHeader feedHeader) {
        final List<FeedItem> feedItems = feedHeader == null ? new ArrayList<FeedItem>() : this.feedItemsRepository.loadItems(feedHeader.id);

        return feedItems == null ? new ArrayList<FeedItem>() : feedItems;
    }

    private Feed fetchFeed(final String feedUrl) throws ControlServiceException {

        try {
            final String data = this.fetcher.fetch(feedUrl);

            return FeedParser.parse(feedUrl, data);
        } catch (final UrlFetcherException exception) {
            throw new ControlServiceException(urlFetcherError(feedUrl), exception);
        } catch (FeedParserException exception) {
            throw new ControlServiceException(feedParseError(feedUrl), exception);
        }
    }

}
