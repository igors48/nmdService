package nmd.rss.collector.controller;

import com.google.appengine.api.datastore.Transaction;
import nmd.rss.collector.Transactions;
import nmd.rss.collector.error.ServiceException;
import nmd.rss.collector.feed.FeedHeader;
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
import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.collector.util.Assert.assertStringIsValid;
import static nmd.rss.collector.util.TransactionTools.rollbackIfActive;

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
        assertNotNull(categoriesRepository);
        this.categoriesRepository = categoriesRepository;

        assertNotNull(readFeedItemsRepository);
        this.readFeedItemsRepository = readFeedItemsRepository;

        assertNotNull(feedHeadersRepository);
        this.feedHeadersRepository = feedHeadersRepository;

        assertNotNull(feedItemsRepository);
        this.feedItemsRepository = feedItemsRepository;

        assertNotNull(transactions);
        this.transactions = transactions;
    }

    public Category addCategory(final String name) {
        assertStringIsValid(name);

        Transaction transaction = null;

        try {
            transaction = this.transactions.beginOne();

            final String trimmed = name.trim();

            final Set<Category> categories = getAllCategoriesWithMain();

            for (final Category category : categories) {

                if (category.name.equalsIgnoreCase(trimmed)) {
                    transaction.commit();

                    return category;
                }
            }

            final Category created = new Category(UUID.randomUUID().toString(), trimmed);

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

            //public static FeedReadReport createFeedReadReport(final FeedHeader header, final List<FeedItem> items, final ReadFeedItems readFeedItems)
            for (final Category category : categories) {
                final List<UUID> feedIds = findFeedIdsForCategory(category.uuid, readFeedItemsList);
                final CategoryReport categoryReport = new CategoryReport(category.uuid, category.name, feedIds, 0, 0, 0);

                reports.add(categoryReport);
            }

            transaction.commit();

            return reports;
        } finally {
            rollbackIfActive(transaction);
        }
    }

    public void assignFeedToCategory(final UUID feedId, final String categoryId) throws ServiceException {
        assertNotNull(feedId);
        assertStringIsValid(categoryId);

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
        assertStringIsValid(categoryId);

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
        assertStringIsValid(categoryId);
        assertStringIsValid(newName);

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
            throw new ServiceException(unknownCategory(categoryId));
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
