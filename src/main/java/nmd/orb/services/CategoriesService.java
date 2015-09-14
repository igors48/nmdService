package nmd.orb.services;

import com.google.appengine.api.datastore.Transaction;
import nmd.orb.error.ServiceException;
import nmd.orb.feed.FeedHeader;
import nmd.orb.feed.FeedItem;
import nmd.orb.reader.Category;
import nmd.orb.reader.ReadFeedItems;
import nmd.orb.repositories.*;
import nmd.orb.services.importer.CategoriesServiceAdapter;
import nmd.orb.services.report.CategoryReport;
import nmd.orb.services.report.ExportReport;
import nmd.orb.services.report.FeedReadReport;

import java.util.*;
import java.util.logging.Logger;

import static nmd.orb.error.ServiceError.*;
import static nmd.orb.feed.FeedHeader.isValidFeedHeaderId;
import static nmd.orb.reader.Category.isValidCategoryId;
import static nmd.orb.reader.Category.isValidCategoryName;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;
import static nmd.orb.util.TransactionTools.rollbackIfActive;

/**
 * @author : igu
 */
public class CategoriesService implements CategoriesServiceAdapter {

    private static final Logger LOGGER = Logger.getLogger(CategoriesService.class.getName());

    private static final CategoryNameComparator CATEGORY_NAME_COMPARATOR = new CategoryNameComparator();
    private static final FeedTitleComparator FEED_TITLE_COMPARATOR = new FeedTitleComparator();

    private final CategoriesRepository categoriesRepository;
    private final ReadFeedItemsRepository readFeedItemsRepository;
    private final FeedHeadersRepository feedHeadersRepository;
    private final FeedItemsRepository feedItemsRepository;

    private final ChangeRegistrationService changeRegistrationService;
    private final UpdateErrorRegistrationService updateErrorRegistrationService;

    private final Transactions transactions;

    public CategoriesService(final CategoriesRepository categoriesRepository, final ReadFeedItemsRepository readFeedItemsRepository, final FeedHeadersRepository feedHeadersRepository, final FeedItemsRepository feedItemsRepository, final ChangeRegistrationService changeRegistrationService, final UpdateErrorRegistrationService updateErrorRegistrationService, final Transactions transactions) {
        guard(notNull(this.categoriesRepository = categoriesRepository));
        guard(notNull(this.readFeedItemsRepository = readFeedItemsRepository));
        guard(notNull(this.feedHeadersRepository = feedHeadersRepository));
        guard(notNull(this.feedItemsRepository = feedItemsRepository));
        guard(notNull(this.changeRegistrationService = changeRegistrationService));
        guard(notNull(this.updateErrorRegistrationService = updateErrorRegistrationService));
        guard(notNull(this.transactions = transactions));
    }

    @Override
    public String createCategory(final String name) {
        guard(isValidCategoryName(name));

        return addCategoryOrFindExistent(name).uuid;
    }

    public Category addCategory(final String name) {
        guard(isValidCategoryName(name));

        Transaction transaction = null;

        try {
            transaction = this.transactions.beginOne();

            final Category result = addCategoryOrFindExistent(name);

            transaction.commit();

            return result;
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

            Collections.sort(reports, CATEGORY_NAME_COMPARATOR);

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
            final FeedHeader feedHeader = loadFeedHeader(feedId);

            final Category category = loadCategory(categoryId);

            final ReadFeedItems readFeedItems = this.readFeedItemsRepository.load(feedId);
            final ReadFeedItems updatedReadFeedItems = readFeedItems.changeCategory(categoryId);

            this.readFeedItemsRepository.store(updatedReadFeedItems);

            this.changeRegistrationService.registerAssignFeedToCategory(feedHeader.title, category.name);

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

                this.changeRegistrationService.registerDeleteCategory(category.name);
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

            this.changeRegistrationService.registerRenameCategory(category.name, newName);

            transaction.commit();
        } finally {
            rollbackIfActive(transaction);
        }
    }

    public ExportReport createExportReport() {
        Transaction transaction = null;

        try {
            transaction = this.transactions.beginOne();

            final ExportReport exportReport = buildExportReport();

            transaction.commit();

            return exportReport;
        } finally {
            rollbackIfActive(transaction);
        }
    }

    public ExportReport buildExportReport() {
        final Set<Category> categories = getAllCategoriesWithMain();
        final List<FeedHeader> headers = this.feedHeadersRepository.loadHeaders();
        final List<ReadFeedItems> readFeedItems = this.readFeedItemsRepository.loadAll();

        return createExportReport(categories, headers, readFeedItems);
    }

    private Category addCategoryOrFindExistent(final String name) {
        final Category result;

        final Set<Category> categories = getAllCategoriesWithMain();
        final Category exists = findByName(name, categories);

        if (exists == null) {
            final Category created = new Category(UUID.randomUUID().toString(), name);

            this.categoriesRepository.store(created);

            result = created;

            this.changeRegistrationService.registerAddCategory(created.name);
        } else {
            result = exists;
        }

        return result;
    }

    private CategoryReport createCategoryReport(final List<ReadFeedItems> readFeedItemsList, final Category category) {
        int read = 0;
        int notRead = 0;
        int readLater = 0;

        final List<FeedReadReport> feedReadReports = new ArrayList<>();

        for (final ReadFeedItems readFeedItems : readFeedItemsList) {

            if (readFeedItems.categoryId.equals(category.uuid)) {
                long current = System.currentTimeMillis();

                final FeedHeader feedHeader = this.feedHeadersRepository.loadHeader(readFeedItems.feedId);
                current = logTime("loadHeader " + readFeedItems.feedId, current);
                final List<FeedItem> feedItems = this.feedItemsRepository.loadItems(readFeedItems.feedId);
                current = logTime("loadItems " + readFeedItems.feedId, current);
                final int sequentialErrorsCount = this.updateErrorRegistrationService.getErrorCount(readFeedItems.feedId);
                current = logTime("loadErrors " + readFeedItems.feedId, current);

                final FeedReadReport feedReadReport = ReadsService.createFeedReadReport(feedHeader, feedItems, readFeedItems, sequentialErrorsCount);

                read += feedReadReport.read;
                notRead += feedReadReport.notRead;
                readLater += feedReadReport.readLater;

                feedReadReports.add(feedReadReport);
            }
        }

        Collections.sort(feedReadReports, FEED_TITLE_COMPARATOR);

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

    public static ExportReport createExportReport(final Set<Category> categories, final List<FeedHeader> headers, final List<ReadFeedItems> readFeedItems) {
        guard(notNull(categories));
        guard(notNull(headers));
        guard(notNull(readFeedItems));

        final Map<Category, Set<FeedHeader>> report = new HashMap<>();

        for (final Category category : categories) {
            report.put(category, new HashSet<FeedHeader>());
        }

        final Set<ReadFeedItems> lostLinks = new HashSet<>();

        for (final ReadFeedItems current : readFeedItems) {
            final String categoryId = current.categoryId;
            final UUID feedId = current.feedId;

            final Category category = findById(categoryId, categories);
            final FeedHeader header = pick(feedId, headers);

            if (category != null && header != null) {
                Set<FeedHeader> headerSet = report.get(category);

                if (headerSet == null) {
                    headerSet = new HashSet<>();
                    report.put(category, headerSet);
                }

                headerSet.add(header);
            } else {
                lostLinks.add(current);
            }
        }

        final Set<FeedHeader> lostHeaders = new HashSet<>();
        lostHeaders.addAll(headers);

        return new ExportReport(report, lostLinks, lostHeaders);
    }

    private static Category findByName(final String name, final Set<Category> categories) {

        for (final Category category : categories) {

            if (category.name.equalsIgnoreCase(name)) {
                return category;
            }
        }

        return null;
    }

    private static Category findById(final String id, final Set<Category> categories) {

        for (final Category current : categories) {

            if (current.uuid.equals(id)) {
                return current;
            }
        }

        return null;
    }

    private static FeedHeader pick(final UUID id, final List<FeedHeader> feedHeaders) {

        for (final FeedHeader current : feedHeaders) {

            if (current.id.equals(id)) {
                feedHeaders.remove(current);

                return current;
            }
        }

        return null;
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

    private static long logTime(String message, long lastTime) {
        final long current = System.currentTimeMillis();

        final long delta = current - lastTime;

        LOGGER.info(message + " : " + delta);

        return current;
    }


    private static class CategoryNameComparator implements Comparator<CategoryReport> {

        @Override
        public int compare(final CategoryReport first, final CategoryReport second) {
            guard(notNull(first));
            guard(notNull(second));

            final String firstName = first.name;
            final String secondName = second.name;

            return firstName.compareTo(secondName);
        }

    }

    private static class FeedTitleComparator implements Comparator<FeedReadReport> {

        @Override
        public int compare(final FeedReadReport first, final FeedReadReport second) {
            guard(notNull(first));
            guard(notNull(second));

            final String firstTitle = first.feedTitle;
            final String secondTitle = second.feedTitle;

            return firstTitle.compareTo(secondTitle);
        }

    }

}
