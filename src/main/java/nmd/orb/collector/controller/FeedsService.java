package nmd.orb.collector.controller;

import com.google.appengine.api.datastore.Transaction;
import nmd.orb.collector.Transactions;
import nmd.orb.collector.error.ServiceException;
import nmd.orb.collector.feed.*;
import nmd.orb.collector.scheduler.FeedUpdateTask;
import nmd.orb.collector.scheduler.FeedUpdateTaskRepository;
import nmd.orb.collector.scheduler.FeedUpdateTaskSchedulerContextRepository;
import nmd.orb.collector.updater.FeedHeadersRepository;
import nmd.orb.collector.updater.FeedItemsRepository;
import nmd.orb.collector.updater.UrlFetcher;
import nmd.orb.reader.CategoriesRepository;
import nmd.orb.reader.Category;
import nmd.orb.reader.ReadFeedItems;
import nmd.orb.reader.ReadFeedItemsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static nmd.orb.collector.error.ServiceError.wrongCategoryId;
import static nmd.orb.collector.feed.FeedHeader.isValidFeedHeaderId;
import static nmd.orb.collector.feed.FeedHeader.isValidFeedHeaderTitle;
import static nmd.orb.reader.Category.isValidCategoryId;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.isValidUrl;
import static nmd.orb.util.Parameter.notNull;
import static nmd.orb.util.TransactionTools.rollbackIfActive;
import static nmd.orb.util.UrlTools.normalizeUrl;

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

        guard(notNull(transactions));
        this.transactions = transactions;

        guard(notNull(feedUpdateTaskRepository));
        this.feedUpdateTaskRepository = feedUpdateTaskRepository;

        guard(notNull(feedUpdateTaskSchedulerContextRepository));
        this.feedUpdateTaskSchedulerContextRepository = feedUpdateTaskSchedulerContextRepository;

        guard(notNull(readFeedItemsRepository));
        this.readFeedItemsRepository = readFeedItemsRepository;

        guard(notNull(categoriesRepository));
        this.categoriesRepository = categoriesRepository;
    }

    public UUID addFeed(final String feedUrl, final String categoryId) throws ServiceException {
        guard(isValidUrl(feedUrl));
        guard(isValidCategoryId(categoryId));

        Transaction transaction = null;

        try {
            transaction = this.transactions.beginOne();

            assertCategoryExists(categoryId);

            final String feedUrlInLowerCase = normalizeUrl(feedUrl);
            final Feed feed = fetchFeed(feedUrlInLowerCase);

            FeedHeader feedHeader = this.feedHeadersRepository.loadHeader(feedUrlInLowerCase);

            if (feedHeader == null) {
                feedHeader = feed.header;
                this.feedHeadersRepository.storeHeader(feedHeader);
            }

            final List<FeedItem> olds = getFeedOldItems(feedHeader);

            createFeedUpdateTask(feedHeader);

            final FeedItemsMergeReport mergeReport = FeedItemsMerger.merge(olds, feed.items, MAX_FEED_ITEMS_COUNT);

            final List<FeedItem> addedAndRetained = mergeReport.getAddedAndRetained();
            final boolean notTheSame = !FeedItemsMerger.listEqualsIgnoringGuid(olds, addedAndRetained);

            if (notTheSame) {
                this.feedItemsRepository.storeItems(feedHeader.id, addedAndRetained);
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
        guard(isValidFeedHeaderId(feedId));
        guard(isValidFeedHeaderTitle(title));

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
        guard(isValidFeedHeaderId(feedId));

        Transaction transaction = null;

        try {
            transaction = this.transactions.beginOne();

            removeFeedComponents(feedId);

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
        guard(isValidFeedHeaderId(feedId));

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
                removeFeedComponents(header.id);
            }

            this.feedUpdateTaskSchedulerContextRepository.clear();

            this.categoriesRepository.clear();

            transaction.commit();
        } finally {
            rollbackIfActive(transaction);
        }
    }

    private void removeFeedComponents(final UUID feedId) {
        this.feedUpdateTaskRepository.deleteTaskForFeedId(feedId);
        this.feedHeadersRepository.deleteHeader(feedId);
        this.feedItemsRepository.deleteItems(feedId);
        this.readFeedItemsRepository.delete(feedId);
    }

    private void assertCategoryExists(final String categoryId) throws ServiceException {

        if (Category.MAIN_CATEGORY_ID.equals(categoryId)) {
            return;
        }

        final Category category = this.categoriesRepository.load(categoryId);

        if (category == null) {
            throw new ServiceException(wrongCategoryId(categoryId));
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
