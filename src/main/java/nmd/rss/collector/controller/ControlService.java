package nmd.rss.collector.controller;

import nmd.rss.collector.Transactions;
import nmd.rss.collector.feed.*;
import nmd.rss.collector.gae.feed.FeedServiceImpl;
import nmd.rss.collector.scheduler.FeedUpdateTask;
import nmd.rss.collector.scheduler.FeedUpdateTaskRepository;
import nmd.rss.collector.updater.FeedHeadersRepository;
import nmd.rss.collector.updater.FeedItemsRepository;
import nmd.rss.collector.updater.UrlFetcher;
import nmd.rss.collector.updater.UrlFetcherException;

import javax.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static nmd.rss.collector.error.ServiceError.feedParseError;
import static nmd.rss.collector.error.ServiceError.urlFetcherError;
import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.collector.util.Assert.assertStringIsValid;
import static nmd.rss.collector.util.TransactionTools.rollbackIfActive;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.05.13
 */
public class ControlService {

    private static final int MAX_FEED_ITEMS_COUNT = 10;

    private final Transactions transactions;

    private final FeedHeadersRepository feedHeadersRepository;
    private final FeedItemsRepository feedItemsRepository;
    private final FeedUpdateTaskRepository feedUpdateTaskRepository;

    private final UrlFetcher fetcher;

    public ControlService(final FeedHeadersRepository feedHeadersRepository, final FeedItemsRepository feedItemsRepository, final FeedUpdateTaskRepository feedUpdateTaskRepository, final UrlFetcher fetcher, final Transactions transactions) {
        assertNotNull(feedHeadersRepository);
        this.feedHeadersRepository = feedHeadersRepository;

        assertNotNull(feedItemsRepository);
        this.feedItemsRepository = feedItemsRepository;

        assertNotNull(feedUpdateTaskRepository);
        this.feedUpdateTaskRepository = feedUpdateTaskRepository;

        assertNotNull(fetcher);
        this.fetcher = fetcher;

        assertNotNull(transactions);
        this.transactions = transactions;
    }

    public UUID addFeed(final String feedUrl) throws ControllerException {
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

            List<FeedItem> olds = getFeedOldItems(feedHeader);

            createFeedUpdateTask(feedHeader);

            final FeedItemsMergeReport mergeReport = FeedItemsMerger.merge(olds, feed.items, MAX_FEED_ITEMS_COUNT);
            FeedServiceImpl.updateFeedItems(feedHeader.id, mergeReport.retained, mergeReport.added, this.feedItemsRepository);

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

            List<FeedHeader> headers = this.feedHeadersRepository.loadHeaders();

            transaction.commit();

            return headers;
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

    private List<FeedItem> getFeedOldItems(final FeedHeader feedHeader) {
        final List<FeedItem> feedItems = feedHeader == null ? new ArrayList<FeedItem>() : this.feedItemsRepository.loadItems(feedHeader.id);

        return feedItems == null ? new ArrayList<FeedItem>() : feedItems;
    }

    private Feed fetchFeed(final String feedUrl) throws ControllerException {

        try {
            final String data = this.fetcher.fetch(feedUrl);

            return FeedParser.parse(feedUrl, data);
        } catch (final UrlFetcherException exception) {
            throw new ControllerException(urlFetcherError(feedUrl), exception);
        } catch (FeedParserException exception) {
            throw new ControllerException(feedParseError(feedUrl), exception);
        }
    }

}
