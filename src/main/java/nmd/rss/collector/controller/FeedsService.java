package nmd.rss.collector.controller;

import com.google.appengine.api.datastore.Transaction;
import nmd.rss.collector.Transactions;
import nmd.rss.collector.error.ServiceException;
import nmd.rss.collector.feed.*;
import nmd.rss.collector.scheduler.FeedUpdateTask;
import nmd.rss.collector.scheduler.FeedUpdateTaskRepository;
import nmd.rss.collector.scheduler.FeedUpdateTaskSchedulerContextRepository;
import nmd.rss.collector.updater.FeedHeadersRepository;
import nmd.rss.collector.updater.FeedItemsRepository;
import nmd.rss.collector.updater.UrlFetcher;
import nmd.rss.reader.CategoriesRepository;
import nmd.rss.reader.Category;
import nmd.rss.reader.ReadFeedItems;
import nmd.rss.reader.ReadFeedItemsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static nmd.rss.collector.error.ServiceError.unknownCategory;
import static nmd.rss.collector.util.Assert.*;
import static nmd.rss.collector.util.Parameter.isValidUrl;
import static nmd.rss.collector.util.TransactionTools.rollbackIfActive;
import static nmd.rss.collector.util.UrlTools.normalizeUrl;
import static nmd.rss.reader.Category.isValidCategoryId;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 02.02.14
 */
public class FeedsService extends AbstractService {

    private static final int MAX_FEED_ITEMS_COUNT = 300;

    private final Transactions transactions;
    private final FeedUpdateTaskRepository feedUpdateTaskRepository;
    private final FeedUpdateTaskSchedulerContextRepository feedUpdateTaskSchedulerContextRepository;
    private final ReadFeedItemsRepository readFeedItemsRepository;
    private final CategoriesRepository categoriesRepository;

    public FeedsService(final FeedHeadersRepository feedHeadersRepository, final FeedItemsRepository feedItemsRepository, final FeedUpdateTaskRepository feedUpdateTaskRepository, final ReadFeedItemsRepository readFeedItemsRepository, final CategoriesRepository categoriesRepository, final FeedUpdateTaskSchedulerContextRepository feedUpdateTaskSchedulerContextRepository, final UrlFetcher fetcher, final Transactions transactions) {
        super(feedHeadersRepository, feedItemsRepository, fetcher);

        assertNotNull(transactions);
        this.transactions = transactions;

        assertNotNull(feedUpdateTaskRepository);
        this.feedUpdateTaskRepository = feedUpdateTaskRepository;

        assertNotNull(feedUpdateTaskSchedulerContextRepository);
        this.feedUpdateTaskSchedulerContextRepository = feedUpdateTaskSchedulerContextRepository;

        assertNotNull(readFeedItemsRepository);
        this.readFeedItemsRepository = readFeedItemsRepository;

        assertNotNull(categoriesRepository);
        this.categoriesRepository = categoriesRepository;
    }

    public UUID addFeed(final String feedUrl, final String categoryId) throws ServiceException {
        guard(isValidUrl(feedUrl));
        guard(isValidCategoryId(categoryId));

        Transaction transaction = null;

        assertCategoryIdValid(categoryId);

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
            final boolean nothingChanged = mergeReport.added.isEmpty() && mergeReport.removed.isEmpty();

            if (!nothingChanged) {
                this.feedItemsRepository.storeItems(feedHeader.id, mergeReport.getAddedAndRetained());
            }

            final ReadFeedItems existsReadItems = this.readFeedItemsRepository.load(feedHeader.id);
            final ReadFeedItems updatedReadItems = existsReadItems.changeCategory(categoryId);
            this.readFeedItemsRepository.store(updatedReadItems);

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

    //TODO move to special service
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

            this.categoriesRepository.clear();

            transaction.commit();
        } finally {
            rollbackIfActive(transaction);
        }
    }

    private void assertCategoryIdValid(final String categoryId) throws ServiceException {

        if (Category.MAIN_CATEGORY_ID.equals(categoryId)) {
            return;
        }

        final Category category = this.categoriesRepository.load(categoryId);

        if (category == null) {
            throw new ServiceException(unknownCategory(categoryId));
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
