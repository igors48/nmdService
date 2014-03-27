package nmd.rss.collector.controller;

import com.google.appengine.api.datastore.Transaction;
import nmd.rss.collector.Transactions;
import nmd.rss.collector.error.ServiceException;
import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.collector.feed.FeedItem;
import nmd.rss.collector.updater.FeedHeadersRepository;
import nmd.rss.collector.updater.FeedItemsRepository;
import nmd.rss.reader.CategoriesRepository;
import nmd.rss.reader.Category;
import nmd.rss.reader.ReadFeedItems;
import nmd.rss.reader.ReadFeedItemsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static nmd.rss.collector.error.ServiceError.*;
import static nmd.rss.collector.feed.FeedHeader.isValidFeedId;
import static nmd.rss.collector.util.Assert.guard;
import static nmd.rss.collector.util.Parameter.notNull;
import static nmd.rss.collector.util.TransactionTools.rollbackIfActive;
import static nmd.rss.reader.Category.isValidCategoryId;
import static nmd.rss.reader.Category.isValidCategoryName;

/**
 * @author : igu
 */
public class CategoriesService {

    private final CategoriesRepository categoriesRepository;
    private final ReadFeedItemsRepository readFeedItemsRepository;
    private final FeedHeadersRepository feedHeadersRepository;
    private final FeedItemsRepository feedItemsRepository;

    private final Transactions transactions;

    public CategoriesService(final CategoriesRepository categoriesRepository, final ReadFeedItemsRepository readFeedItemsRepository, final FeedHeadersRepository feedHeadersRepository, final FeedItemsRepository feedItemsRepository, final Transactions transactions) {
        guard(notNull(categoriesRepository));
        this.categoriesRepository = categoriesRepository;

        guard(notNull(readFeedItemsRepository));
        this.readFeedItemsRepository = readFeedItemsRepository;

        guard(notNull(feedHeadersRepository));
        this.feedHeadersRepository = feedHeadersRepository;

        guard(notNull(feedItemsRepository));
        this.feedItemsRepository = feedItemsRepository;

        guard(notNull(transactions));
        this.transactions = transactions;
    }

    public Category addCategory(final String name) {
        guard(isValidCategoryName(name));

        Transaction transaction = null;

        try {
            transaction = this.transactions.beginOne();

            final Set<Category> categories = getAllCategoriesWithMain();

            for (final Category category : categories) {

                if (category.name.equalsIgnoreCase(name)) {
                    transaction.commit();

                    return category;
                }
            }

            final Category created = new Category(UUID.randomUUID().toString(), name);

            this.categoriesRepository.store(created);

            transaction.commit();

            return created;
        } finally {
            rollbackIfActive(transaction);
        }
    }

    public List<CategoryReport> getCategoriesReport() {
        Transaction transaction = null;

        try {
            final List<CategoryReport> reports = new ArrayList<>();

            transaction = this.transactions.beginOne();

            final Set<Category> categories = getAllCategoriesWithMain();
            final List<ReadFeedItems> readFeedItemsList = this.readFeedItemsRepository.loadAll();

            for (final Category category : categories) {
                final CategoryReport categoryReport = createCategoryReport(readFeedItemsList, category);

                reports.add(categoryReport);
            }

            transaction.commit();

            return reports;
        } finally {
            rollbackIfActive(transaction);
        }
    }

    public void assignFeedToCategory(final UUID feedId, final String categoryId) throws ServiceException {
        guard(isValidFeedId(feedId));
        guard(isValidCategoryId(categoryId));

        Transaction transaction = null;

        try {
            transaction = this.transactions.beginOne();
            loadFeedHeader(feedId);

            loadCategory(categoryId);

            final ReadFeedItems readFeedItems = this.readFeedItemsRepository.load(feedId);
            final ReadFeedItems updatedReadFeedItems = readFeedItems.changeCategory(categoryId);

            this.readFeedItemsRepository.store(updatedReadFeedItems);

            transaction.commit();
        } finally {
            rollbackIfActive(transaction);
        }
    }

    public void deleteCategory(final String categoryId) {
        guard(isValidCategoryId(categoryId));

        if (Category.MAIN_CATEGORY_ID.equals(categoryId)) {
            return;
        }

        Transaction transaction = null;

        try {
            transaction = this.transactions.beginOne();

            final Category category = this.categoriesRepository.load(categoryId);

            if (category != null) {
                final List<ReadFeedItems> readFeedItemsList = this.readFeedItemsRepository.loadAll();
                final List<ReadFeedItems> readFeedItemsListForCategory = findReadFeedItemsForCategory(category.uuid, readFeedItemsList);

                for (final ReadFeedItems items : readFeedItemsListForCategory) {
                    final ReadFeedItems updated = items.changeCategory(Category.MAIN_CATEGORY_ID);

                    this.readFeedItemsRepository.store(updated);
                }

                this.categoriesRepository.delete(categoryId);
            }

            transaction.commit();
        } finally {
            rollbackIfActive(transaction);
        }
    }

    public void renameCategory(final String categoryId, final String newName) throws ServiceException {
        guard(isValidCategoryId(categoryId));
        guard(isValidCategoryName(newName));

        if (Category.MAIN_CATEGORY_ID.equals(categoryId)) {
            return;
        }

        final String trimmed = newName.trim();

        assertCategoryNameUnique(trimmed, categoryId);

        Transaction transaction = null;

        try {
            transaction = this.transactions.beginOne();

            final Category category = loadCategory(categoryId);
            final Category renamed = category.changeName(trimmed);

            this.categoriesRepository.store(renamed);

            transaction.commit();
        } finally {
            rollbackIfActive(transaction);
        }
    }

    private CategoryReport createCategoryReport(final List<ReadFeedItems> readFeedItemsList, final Category category) {
        final List<UUID> feedIds = findFeedIdsForCategory(category.uuid, readFeedItemsList);

        int read = 0;
        int notRead = 0;
        int readLater = 0;

        for (final UUID feedId : feedIds) {
            final FeedHeader feedHeader = this.feedHeadersRepository.loadHeader(feedId);
            final List<FeedItem> feedItems = this.feedItemsRepository.loadItems(feedId);
            final ReadFeedItems readFeedItems = this.readFeedItemsRepository.load(feedId);

            final FeedReadReport feedReadReport = ReadsService.createFeedReadReport(feedHeader, feedItems, readFeedItems);

            read += feedReadReport.read;
            notRead += feedReadReport.notRead;
            readLater += feedReadReport.readLater;
        }

        return new CategoryReport(category.uuid, category.name, feedIds, read, notRead, readLater);
    }

    private void assertCategoryNameUnique(String name, String id) throws ServiceException {
        final Set<Category> categories = getAllCategoriesWithMain();

        for (final Category category : categories) {

            if (category.name.equalsIgnoreCase(name) && !category.uuid.equals(id)) {
                throw new ServiceException(categoryAlreadyExists(name));
            }
        }
    }

    private Category loadCategory(String categoryId) throws ServiceException {
        final Category category = this.categoriesRepository.load(categoryId);

        if (category == null) {
            throw new ServiceException(wrongCategoryId(categoryId));
        }

        return category;
    }

    private Set<Category> getAllCategoriesWithMain() {
        final Set<Category> categories = this.categoriesRepository.loadAll();

        categories.add(Category.MAIN);

        return categories;
    }

    private static List<UUID> findFeedIdsForCategory(final String categoryId, final List<ReadFeedItems> readFeedItemsList) {
        final List<UUID> feedIds = new ArrayList<>();

        for (final ReadFeedItems readFeedItems : readFeedItemsList) {

            if (readFeedItems.categoryId.equals(categoryId)) {
                feedIds.add(readFeedItems.feedId);
            }
        }

        return feedIds;
    }

    private List<ReadFeedItems> findReadFeedItemsForCategory(final String categoryId, final List<ReadFeedItems> readFeedItemsList) {
        final List<ReadFeedItems> list = new ArrayList<>();

        for (final ReadFeedItems readFeedItems : readFeedItemsList) {

            if (readFeedItems.categoryId.equals(categoryId)) {
                list.add(readFeedItems);
            }
        }

        return list;
    }

    private FeedHeader loadFeedHeader(final UUID feedId) throws ServiceException {
        FeedHeader header = this.feedHeadersRepository.loadHeader(feedId);

        if (header == null) {
            throw new ServiceException(wrongFeedId(feedId));
        }

        return header;
    }

}
