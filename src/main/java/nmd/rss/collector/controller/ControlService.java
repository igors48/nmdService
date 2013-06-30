package nmd.rss.collector.controller;

import nmd.rss.collector.Transactions;
import nmd.rss.collector.feed.*;
import nmd.rss.collector.scheduler.FeedUpdateTask;
import nmd.rss.collector.scheduler.FeedUpdateTaskRepository;
import nmd.rss.collector.scheduler.FeedUpdateTaskScheduler;
import nmd.rss.collector.updater.FeedHeadersRepository;
import nmd.rss.collector.updater.FeedItemsRepository;
import nmd.rss.collector.updater.UrlFetcher;
import nmd.rss.collector.updater.UrlFetcherException;

import javax.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static nmd.rss.collector.error.ServiceError.*;
import static nmd.rss.collector.feed.TimestampAscendingComparator.TIMESTAMP_ASCENDING_COMPARATOR;
import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.collector.util.Assert.assertStringIsValid;
import static nmd.rss.collector.util.TransactionTools.rollbackIfActive;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.05.13
 */
public class ControlService {

    private static final int MAX_FEED_ITEMS_COUNT = 1000;

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

        EntityTransaction transaction = null;

        final String feedUrlInLowerCase = feedUrl.toLowerCase();
        final Feed feed = fetchFeed(feedUrlInLowerCase);

        try {
            transaction = this.transactions.getOne();
            transaction.begin();

            FeedHeader feedHeader = this.feedHeadersRepository.loadHeader(feedUrlInLowerCase);

            if (feedHeader == null) {
                feedHeader = feed.header;
                this.feedHeadersRepository.storeHeader(feedHeader);
            }

            final List<FeedItem> olds = getFeedOldItems(feedHeader);

            createFeedUpdateTask(feedHeader);

            final FeedItemsMergeReport mergeReport = FeedItemsMerger.merge(olds, feed.items, MAX_FEED_ITEMS_COUNT);
            updateFeedItems(feedHeader.id, mergeReport.retained, mergeReport.added, this.feedItemsRepository);

            transaction.commit();

            return feedHeader.id;
        } finally {
            rollbackIfActive(transaction);
        }
    }

    public void removeFeed(final UUID feedId) {
        assertNotNull(feedId);

        EntityTransaction transaction = null;

        try {
            transaction = this.transactions.getOne();
            transaction.begin();

            this.feedUpdateTaskRepository.deleteTaskForFeedId(feedId);
            this.feedHeadersRepository.deleteHeader(feedId);
            this.feedItemsRepository.deleteItems(feedId);

            transaction.commit();
        } finally {
            rollbackIfActive(transaction);
        }
    }

    public List<FeedHeader> getFeedHeaders() {
        EntityTransaction transaction = null;

        try {
            transaction = this.transactions.getOne();
            transaction.begin();

            final List<FeedHeader> headers = this.feedHeadersRepository.loadHeaders();

            transaction.commit();

            return headers;
        } finally {
            rollbackIfActive(transaction);
        }
    }

    public Feed getFeed(final UUID feedId) throws ControlServiceException {
        assertNotNull(feedId);

        EntityTransaction transaction = null;

        try {
            transaction = this.transactions.getOne();
            transaction.begin();

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

    public FeedItemsMergeReport updateFeed(final UUID feedId) throws ControlServiceException {
        assertNotNull(feedId);

        EntityTransaction getFeedHeaderAndTaskTransaction = null;

        final FeedHeader header;
        final FeedUpdateTask updateTask;

        try {
            getFeedHeaderAndTaskTransaction = this.transactions.getOne();
            getFeedHeaderAndTaskTransaction.begin();

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

        EntityTransaction updateFeedTransaction = null;

        try {
            updateFeedTransaction = this.transactions.getOne();
            updateFeedTransaction.begin();

            List<FeedItem> olds = getFeedOldItems(header);

            final FeedItemsMergeReport mergeReport = FeedItemsMerger.merge(olds, feed.items, updateTask.maxFeedItemsCount);
            updateFeedItems(header.id, mergeReport.retained, mergeReport.added, this.feedItemsRepository);

            updateFeedTransaction.commit();

            return mergeReport;
        } finally {
            rollbackIfActive(updateFeedTransaction);
        }
    }

    public FeedItemsMergeReport updateCurrentFeed() throws ControlServiceException {
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

    private static void updateFeedItems(final UUID feedId, final List<FeedItem> retained, final List<FeedItem> added, final FeedItemsRepository feedItemsRepository) {
        assertNotNull(feedId);
        assertNotNull(retained);
        assertNotNull(added);
        assertNotNull(feedItemsRepository);

        final List<FeedItem> feedItems = new ArrayList<>();
        feedItems.addAll(retained);
        feedItems.addAll(added);

        Collections.sort(feedItems, TIMESTAMP_ASCENDING_COMPARATOR);

        feedItemsRepository.updateItems(feedId, feedItems);
    }

}
