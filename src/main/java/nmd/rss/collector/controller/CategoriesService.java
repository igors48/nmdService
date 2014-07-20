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
import static nmd.rss.collector.feed.FeedHeader.isValidFeedHeaderId;
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

    public CategoryReport getCategoryReport(final String categoryId) throws ServiceException {
        guard(isValidCategoryId(categoryId));

        Transaction transaction = null;

        try {
            transaction = this.transactions.beginOne();

            final Category category = loadCategory(categoryId);
            final List<ReadFeedItems> readFeedItemsList = this.readFeedItemsRepository.loadAll();
            final CategoryReport categoryReport = createCategoryReport(readFeedItemsList, category);

            transaction.commit();

            return categoryReport;
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
        guard(isValidFeedHeaderId(feedId));
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

            this.categoriesRepository.delete(categoryId);
            this.categoriesRepository.store(renamed);

            transaction.commit();
        } finally {
            rollbackIfActive(transaction);
        }
    }

    private CategoryReport createCategoryReport(final List<ReadFeedItems> readFeedItemsList, final Category category) {
        int read = 0;
        int notRead = 0;
        int readLater = 0;

        final List<FeedReadReport> feedReadReports = new ArrayList<>();

        for (final ReadFeedItems readFeedItems : readFeedItemsList) {

            if (readFeedItems.categoryId.equals(category.uuid)) {
                final FeedHeader feedHeader = this.feedHeadersRepository.loadHeader(readFeedItems.feedId);
                final List<FeedItem> feedItems = this.feedItemsRepository.loadItems(readFeedItems.feedId);

                final FeedReadReport feedReadReport = ReadsService.createFeedReadReport(feedHeader, feedItems, readFeedItems);

                read += feedReadReport.read;
                notRead += feedReadReport.notRead;
                readLater += feedReadReport.readLater;

                feedReadReports.add(feedReadReport);
            }
        }

        return new CategoryReport(category.uuid, category.name, feedReadReports, read, notRead, readLater);
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

        if (categoryId.equals(Category.MAIN_CATEGORY_ID)) {
            return Category.MAIN;
        }

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

    private FeedHeader loadFeedHeader(final UUID feedId) throws ServiceException {
        FeedHeader header = this.feedHeadersRepository.loadHeader(feedId);

        if (header == null) {
            throw new ServiceException(wrongFeedId(feedId));
        }

        return header;
    }

    private static List<ReadFeedItems> findReadFeedItemsForCategory(final String categoryId, final List<ReadFeedItems> readFeedItemsList) {
        final List<ReadFeedItems> list = new ArrayList<>();

        for (final ReadFeedItems readFeedItems : readFeedItemsList) {

            if (readFeedItems.categoryId.equals(categoryId)) {
                list.add(readFeedItems);
            }
        }

        return list;
    }

}
