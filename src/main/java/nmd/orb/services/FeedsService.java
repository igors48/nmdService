package nmd.orb.services;

import com.google.appengine.api.datastore.Transaction;
import nmd.orb.collector.fetcher.UrlFetcher;
import nmd.orb.collector.merger.FeedItemsMergeReport;
import nmd.orb.collector.merger.FeedItemsMerger;
import nmd.orb.collector.scheduler.FeedUpdateTask;
import nmd.orb.error.ServiceException;
import nmd.orb.feed.Feed;
import nmd.orb.feed.FeedHeader;
import nmd.orb.feed.FeedItem;
import nmd.orb.reader.Category;
import nmd.orb.reader.ReadFeedItems;
import nmd.orb.repositories.*;
import nmd.orb.services.importer.FeedsServiceAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static nmd.orb.error.ServiceError.wrongCategoryId;
import static nmd.orb.feed.FeedHeader.isValidFeedHeaderId;
import static nmd.orb.feed.FeedHeader.isValidFeedHeaderTitle;
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
public class FeedsService extends AbstractService implements FeedsServiceAdapter {

    private static final int MAX_FEED_ITEMS_COUNT = 300;

    private final Transactions transactions;
    private final FeedUpdateTaskRepository feedUpdateTaskRepository;
    private final ReadFeedItemsRepository readFeedItemsRepository;
    private final CategoriesRepository categoriesRepository;

    private final ChangeRegistrationService changeRegistrationService;
    private final UpdateErrorRegistrationService updateErrorRegistrationService;

    public FeedsService(final FeedHeadersRepository feedHeadersRepository, final FeedItemsRepository feedItemsRepository, final FeedUpdateTaskRepository feedUpdateTaskRepository, final ReadFeedItemsRepository readFeedItemsRepository, final CategoriesRepository categoriesRepository, final ChangeRegistrationService changeRegistrationService, final UpdateErrorRegistrationService updateErrorRegistrationService, final UrlFetcher fetcher, final Transactions transactions) {
        super(feedHeadersRepository, feedItemsRepository, fetcher);

        guard(notNull(transactions));
        this.transactions = transactions;

        guard(notNull(feedUpdateTaskRepository));
        this.feedUpdateTaskRepository = feedUpdateTaskRepository;

        guard(notNull(readFeedItemsRepository));
        this.readFeedItemsRepository = readFeedItemsRepository;

        guard(notNull(categoriesRepository));
        this.categoriesRepository = categoriesRepository;

        guard(notNull(changeRegistrationService));
        this.changeRegistrationService = changeRegistrationService;

        guard(notNull(updateErrorRegistrationService));
        this.updateErrorRegistrationService = updateErrorRegistrationService;
    }

    public UUID addFeed(final String feedLink, final String categoryId) throws ServiceException {
        guard(isValidUrl(feedLink));
        guard(isValidCategoryId(categoryId));

        return addFeed(feedLink, "", categoryId);
    }

    @Override
    public UUID addFeed(final String feedUrl, final String feedTitle, final String categoryId) throws ServiceException {
        guard(isValidUrl(feedUrl));
        guard(feedTitle.isEmpty() || isValidFeedHeaderTitle(feedTitle));
        guard(isValidCategoryId(categoryId));

        Transaction transaction = null;

        try {
            final String feedUrlInLowerCase = normalizeUrl(feedUrl);
            final Feed feed = fetchFeed(feedUrlInLowerCase);

            transaction = this.transactions.beginOne();

            final FeedHeader feedHeader = createFeed(feed, feedTitle, categoryId);

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
            renameFeed(title, oldHeader);

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

            removeFeedComponents(feedId, this.feedUpdateTaskRepository, this.feedHeadersRepository, this.feedItemsRepository, this.readFeedItemsRepository, this.changeRegistrationService, this.updateErrorRegistrationService);

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

    private void renameFeed(final String title, final FeedHeader header) {
        final FeedHeader newHeader = header.changeTitle(title);

        this.feedHeadersRepository.deleteHeader(header.id);
        this.feedHeadersRepository.storeHeader(newHeader);

        this.changeRegistrationService.registerChange();
    }

    private FeedHeader createFeed(final Feed feed, final String feedTitle, final String categoryId) throws ServiceException {
        assertCategoryExists(categoryId);

        final String feedLink = feed.header.feedLink;
        FeedHeader feedHeader = this.feedHeadersRepository.loadHeader(feedLink);

        if (feedHeader == null) {
            feedHeader = feedTitle.isEmpty() ? feed.header : feed.header.changeTitle(feedTitle);
            this.feedHeadersRepository.storeHeader(feedHeader);
        }

        final List<FeedItem> olds = getFeedOldItems(feedHeader);

        createFeedUpdateTask(feedHeader);

        final FeedItemsMergeReport mergeReport = FeedItemsMerger.merge(olds, feed.items, MAX_FEED_ITEMS_COUNT);

        final List<FeedItem> addedAndRetained = mergeReport.getAddedAndRetained();
        this.feedItemsRepository.storeItems(feedHeader.id, addedAndRetained);

        final ReadFeedItems existsReadItems = this.readFeedItemsRepository.load(feedHeader.id);
        final ReadFeedItems updatedReadItems = existsReadItems.changeCategory(categoryId);
        this.readFeedItemsRepository.store(updatedReadItems);

        this.changeRegistrationService.registerChange();

        return feedHeader;
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

    public static void removeFeedComponents(final UUID feedId, final FeedUpdateTaskRepository feedUpdateTaskRepository, final FeedHeadersRepository feedHeadersRepository, final FeedItemsRepository feedItemsRepository, final ReadFeedItemsRepository readFeedItemsRepository, final ChangeRegistrationService changeRegistrationService, final UpdateErrorRegistrationService updateErrorRegistrationService) {
        guard(notNull(feedId));
        guard(notNull(feedUpdateTaskRepository));
        guard(notNull(feedHeadersRepository));
        guard(notNull(feedItemsRepository));
        guard(notNull(readFeedItemsRepository));
        guard(notNull(changeRegistrationService));
        guard(notNull(updateErrorRegistrationService));

        feedUpdateTaskRepository.deleteTaskForFeedId(feedId);
        feedHeadersRepository.deleteHeader(feedId);
        feedItemsRepository.deleteItems(feedId);
        readFeedItemsRepository.delete(feedId);
        updateErrorRegistrationService.delete(feedId);

        changeRegistrationService.registerChange();
    }

}
