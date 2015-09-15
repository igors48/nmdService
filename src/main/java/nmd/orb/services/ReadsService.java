package nmd.orb.services;

import com.google.appengine.api.datastore.Transaction;
import nmd.orb.collector.fetcher.UrlFetcher;
import nmd.orb.content.ContentElement;
import nmd.orb.error.ServiceException;
import nmd.orb.feed.FeedHeader;
import nmd.orb.feed.FeedItem;
import nmd.orb.feed.FeedItemShortcut;
import nmd.orb.reader.FeedItemsComparisonReport;
import nmd.orb.reader.ReadFeedItems;
import nmd.orb.repositories.FeedHeadersRepository;
import nmd.orb.repositories.FeedItemsRepository;
import nmd.orb.repositories.ReadFeedItemsRepository;
import nmd.orb.repositories.Transactions;
import nmd.orb.services.filter.FeedItemReportFilter;
import nmd.orb.services.report.FeedItemReport;
import nmd.orb.services.report.FeedItemsCardsReport;
import nmd.orb.services.report.FeedItemsReport;
import nmd.orb.services.report.FeedReadReport;
import nmd.orb.services.update.UpdateErrors;
import nmd.orb.sources.Source;
import nmd.orb.util.Direction;
import nmd.orb.util.Page;

import java.util.*;

import static nmd.orb.collector.merger.TimestampAscendingComparatorForShortcut.TIMESTAMP_ASCENDING_COMPARATOR_FOR_SHORTCUT;
import static nmd.orb.collector.merger.TimestampDescendingComparator.TIMESTAMP_DESCENDING_COMPARATOR;
import static nmd.orb.content.ContentRenderer.render;
import static nmd.orb.content.ContentTransformer.transform;
import static nmd.orb.feed.FeedHeader.isValidFeedHeaderId;
import static nmd.orb.feed.FeedItem.isValidFeedItemGuid;
import static nmd.orb.feed.FeedItem.isValidLastUsedFeedItemGuid;
import static nmd.orb.reader.FeedItemsComparator.compare;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Page.isValidKeyItemGuid;
import static nmd.orb.util.Parameter.*;
import static nmd.orb.util.TransactionTools.rollbackIfActive;
import static nmd.orb.util.UrlTools.getBaseLink;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 02.02.14
 */
public class ReadsService extends AbstractService {

    private final Transactions transactions;
    private final ReadFeedItemsRepository readFeedItemsRepository;

    private final UpdateErrorRegistrationService updateErrorRegistrationService;

    public ReadsService(final FeedHeadersRepository feedHeadersRepository, final FeedItemsRepository feedItemsRepository, final ReadFeedItemsRepository readFeedItemsRepository, final UpdateErrorRegistrationService updateErrorRegistrationService, final UrlFetcher fetcher, final Transactions transactions) {
        super(feedHeadersRepository, feedItemsRepository, fetcher);

        guard(notNull(this.transactions = transactions));
        guard(notNull(this.readFeedItemsRepository = readFeedItemsRepository));
        guard(notNull(this.updateErrorRegistrationService = updateErrorRegistrationService));
    }

    public List<FeedReadReport> getFeedsReadReport() {
        Transaction transaction = null;

        try {
            transaction = this.transactions.beginOne();

            final List<FeedHeader> headers = this.feedHeadersRepository.loadHeaders();
            final List<FeedReadReport> report = new ArrayList<>();

            for (final FeedHeader header : headers) {
                final List<FeedItemShortcut> shortcuts = this.feedItemsRepository.loadItemsShortcuts(header.id);
                final ReadFeedItems readFeedItems = this.readFeedItemsRepository.load(header.id);
                final int sequentialErrorsCount = this.updateErrorRegistrationService.getErrorCount(header.id);

                final FeedReadReport feedReadReport = createFeedReadReport(header, shortcuts, readFeedItems, sequentialErrorsCount);

                report.add(feedReadReport);
            }

            transaction.commit();

            return report;
        } finally {
            rollbackIfActive(transaction);
        }
    }

    public FeedItemsReport getFeedItemsReport(final UUID feedId, final FeedItemReportFilter filter, final String lastUsedFeedItemId) throws ServiceException {
        guard(isValidFeedHeaderId(feedId));
        guard(notNull(filter));
        guard(isValidLastUsedFeedItemGuid(lastUsedFeedItemId));

        Transaction transaction = null;

        try {
            transaction = this.transactions.beginOne();

            final FeedHeader header = loadFeedHeader(feedId);

            final ArrayList<FeedItemReport> feedItemReports = new ArrayList<>();

            final List<FeedItem> feedItems = this.feedItemsRepository.loadItems(feedId);
            final int total = feedItems.size();
            Collections.sort(feedItems, TIMESTAMP_DESCENDING_COMPARATOR);

            final FeedItem topItem = feedItems.isEmpty() ? null : feedItems.get(0);
            final Date topItemDate = topItem == null ? new Date() : topItem.date;

            final ReadFeedItems readFeedItems = this.readFeedItemsRepository.load(feedId);

            int read = 0;
            int notRead = 0;
            int readLater = 0;
            int addedSinceLastView = 0;

            for (final FeedItem feedItem : feedItems) {
                final int index = feedItems.indexOf(feedItem);
                final FeedItemReport feedItemReport = getFeedItemReport(feedId, header.link, readFeedItems, feedItem, index, total);

                final boolean acceptableById = feedItemReport.itemId.equals(lastUsedFeedItemId);
                final boolean acceptableByFilter = filter.acceptable(feedItemReport);
                final boolean acceptable = acceptableById || acceptableByFilter;

                if (acceptable) {
                    feedItemReports.add(feedItemReport);
                }

                if (feedItemReport.read) {
                    ++read;
                } else {
                    ++notRead;
                }

                if (feedItemReport.readLater) {
                    ++readLater;
                }

                if (feedItemReport.addedSinceLastView) {
                    ++addedSinceLastView;
                }
            }

            final UpdateErrors updateErrors = this.updateErrorRegistrationService.load(feedId);

            transaction.commit();

            return new FeedItemsReport(header.id, header.title, header.feedLink, read, notRead, readLater, addedSinceLastView, feedItemReports, readFeedItems.lastUpdate, topItemDate, updateErrors.errors);
        } finally {
            rollbackIfActive(transaction);
        }
    }

    public FeedItemsCardsReport getFeedItemsCardsReport(final UUID feedId, final String itemId, final int size, final Direction direction) throws ServiceException {
        guard(isValidFeedHeaderId(feedId));
        guard(isValidKeyItemGuid(itemId));
        guard(isPositive(size));
        guard(notNull(direction));

        Transaction transaction = null;

        try {
            transaction = this.transactions.beginOne();

            final FeedHeader header = loadFeedHeader(feedId);

            final ArrayList<FeedItemReport> feedItemReports = new ArrayList<>();

            final List<FeedItem> feedItems = this.feedItemsRepository.loadItems(feedId);
            final int total = feedItems.size();

            Collections.sort(feedItems, TIMESTAMP_DESCENDING_COMPARATOR);

            final FeedItem topItem = feedItems.isEmpty() ? null : feedItems.get(0);
            final Date topItemDate = topItem == null ? new Date() : topItem.date;

            final Page<FeedItem> page = Page.create(feedItems, itemId, size, direction);

            final ReadFeedItems readFeedItems = this.readFeedItemsRepository.load(feedId);

            for (final FeedItem feedItem : page.items) {
                final int index = feedItems.indexOf(feedItem);
                final FeedItemReport feedItemReport = getFeedItemReport(feedId, header.link, readFeedItems, feedItem, index, total);

                feedItemReports.add(feedItemReport);
            }

            transaction.commit();

            return new FeedItemsCardsReport(header.id, header.title, page.first, page.last, feedItemReports, topItemDate);
        } finally {
            rollbackIfActive(transaction);
        }
    }

    public void toggleReadLaterItemMark(final UUID feedId, final String itemId) throws ServiceException {
        guard(isValidFeedHeaderId(feedId));
        guard(isValidFeedItemGuid(itemId));

        Transaction transaction = null;

        try {
            transaction = this.transactions.beginOne();

            loadFeedHeader(feedId);

            final ReadFeedItems readFeedItems = this.readFeedItemsRepository.load(feedId);
            final Set<String> storedGuids = getStoredGuids(feedId);

            final Set<String> backedReadLaterItemIds = new HashSet<>();
            backedReadLaterItemIds.addAll(readFeedItems.readLaterItemIds);
            backedReadLaterItemIds.retainAll(storedGuids);

            if (storedGuids.contains(itemId)) {

                if (backedReadLaterItemIds.contains(itemId)) {
                    backedReadLaterItemIds.remove(itemId);
                } else {
                    backedReadLaterItemIds.add(itemId);
                }

                final ReadFeedItems updatedReadFeedItems = new ReadFeedItems(feedId, readFeedItems.lastUpdate, readFeedItems.readItemIds, backedReadLaterItemIds, readFeedItems.categoryId);
                this.readFeedItemsRepository.store(updatedReadFeedItems);
            }

            transaction.commit();
        } finally {
            rollbackIfActive(transaction);
        }
    }

    public void markItemAsRead(final UUID feedId, final String itemId) throws ServiceException {
        guard(isValidFeedHeaderId(feedId));
        guard(isValidFeedItemGuid(itemId));

        Transaction transaction = null;

        try {
            transaction = this.transactions.beginOne();

            loadFeedHeader(feedId);

            final List<FeedItem> items = this.feedItemsRepository.loadItems(feedId);

            final FeedItem feedItem = find(itemId, items);

            if (feedItem == null) {
                return;
            }

            final Set<String> storedGuids = getStoredGuids(items);
            final ReadFeedItems readFeedItems = this.readFeedItemsRepository.load(feedId);

            final Set<String> readGuids = new HashSet<>();
            readGuids.addAll(readFeedItems.readItemIds);
            readGuids.add(itemId);

            final Set<String> readLaterGuids = new HashSet<>();
            readLaterGuids.addAll(readFeedItems.readLaterItemIds);
            readLaterGuids.remove(itemId);

            final FeedItemsComparisonReport comparisonReport = compare(readGuids, storedGuids);

            final Date lastUpdate = readFeedItems.lastUpdate.compareTo(feedItem.date) > 0 ? readFeedItems.lastUpdate : feedItem.date;
            final ReadFeedItems updatedReadFeedItems = new ReadFeedItems(feedId, lastUpdate, comparisonReport.readItems, readLaterGuids, readFeedItems.categoryId);

            this.readFeedItemsRepository.store(updatedReadFeedItems);

            transaction.commit();
        } finally {
            rollbackIfActive(transaction);
        }
    }

    public void markItemAsNotRead(final UUID feedId, final String itemId) throws ServiceException {
        guard(isValidFeedHeaderId(feedId));
        guard(isValidFeedItemGuid(itemId));

        Transaction transaction = null;

        try {
            transaction = this.transactions.beginOne();

            loadFeedHeader(feedId);

            final List<FeedItem> items = this.feedItemsRepository.loadItems(feedId);

            final FeedItem feedItem = find(itemId, items);

            if (feedItem == null) {
                return;
            }

            final Set<String> storedGuids = getStoredGuids(items);
            final ReadFeedItems readFeedItems = this.readFeedItemsRepository.load(feedId);

            final Set<String> readGuids = new HashSet<>();
            readGuids.addAll(readFeedItems.readItemIds);
            readGuids.remove(itemId);

            final Set<String> readLaterGuids = new HashSet<>();
            readLaterGuids.addAll(readFeedItems.readLaterItemIds);

            final FeedItemsComparisonReport comparisonReport = compare(readGuids, storedGuids);

            final List<FeedItem> readItems = find(readGuids, items);
            final FeedItem youngest = findYoungest(readItems);
            final Date lastUpdate = youngest == null ? new Date(0) : youngest.date;

            final ReadFeedItems updatedReadFeedItems = new ReadFeedItems(feedId, lastUpdate, comparisonReport.readItems, readLaterGuids, readFeedItems.categoryId);

            this.readFeedItemsRepository.store(updatedReadFeedItems);

            transaction.commit();
        } finally {
            rollbackIfActive(transaction);
        }
    }

    public void markAllItemsAsRead(final UUID feedId, long topItemTimestamp) throws ServiceException {
        guard(isValidFeedHeaderId(feedId));
        guard(isPositive(topItemTimestamp));

        Transaction transaction = null;

        try {
            transaction = this.transactions.beginOne();

            loadFeedHeader(feedId);

            final Set<String> readGuids = new HashSet<>();
            final Set<String> readLaterGuids = new HashSet<>();

            final List<FeedItem> items = this.feedItemsRepository.loadItems(feedId);
            final FeedItem youngest = findYoungest(items);
            final Set<String> storedGuids = getStoredGuids(items, topItemTimestamp);
            readGuids.addAll(storedGuids);

            final ReadFeedItems readFeedItems = this.readFeedItemsRepository.load(feedId);
            readLaterGuids.addAll(readFeedItems.readLaterItemIds);

            final Date youngestDate = youngest == null ? new Date() : youngest.date;
            final Date lastUpdate = topItemTimestamp == 0 ? youngestDate : new Date(topItemTimestamp);

            final ReadFeedItems updatedReadFeedItems = new ReadFeedItems(feedId, lastUpdate, readGuids, readLaterGuids, readFeedItems.categoryId);

            this.readFeedItemsRepository.store(updatedReadFeedItems);

            transaction.commit();
        } finally {
            rollbackIfActive(transaction);
        }
    }

    private Set<String> getStoredGuids(final UUID feedId) {
        final List<FeedItem> items = this.feedItemsRepository.loadItems(feedId);

        return getStoredGuids(items);
    }

    public static FeedReadReport createFeedReadReport(final FeedHeader header, final List<FeedItemShortcut> shortcuts, final ReadFeedItems readFeedItems, final int sequentialErrorsCount) {
        guard(notNull(header));
        guard(notNull(shortcuts));
        guard(notNull(readFeedItems));

        final Set<String> storedGuids = getStoredGuidsFromShortcuts(shortcuts);

        final FeedItemsComparisonReport comparisonReport = compare(readFeedItems.readItemIds, storedGuids);

        final FeedItemShortcut topItem = findFirstNotReadFeedItem(shortcuts, readFeedItems.readItemIds, readFeedItems.lastUpdate);
        final String topItemId = topItem == null ? null : topItem.guid;
        final String topItemLink = topItem == null ? null : topItem.gotoLink;

        final int addedFromLastVisit = countYoungerItems(shortcuts, readFeedItems.lastUpdate);

        final int readLaterItemsCount = countReadLaterItems(shortcuts, readFeedItems.readLaterItemIds);

        final Source feedType = Source.detect(header.feedLink);

        final boolean hasErrors = sequentialErrorsCount >= UpdateErrorRegistrationService.MAX_SEQUENTIAL_UPDATE_ERRORS_COUNT;

        return new FeedReadReport(header.id, feedType, header.title, comparisonReport.readItems.size(), comparisonReport.newItems.size(), readLaterItemsCount, addedFromLastVisit, topItemId, topItemLink, readFeedItems.lastUpdate, hasErrors);
    }

    public static FeedItemShortcut findFirstNotReadFeedItem(final List<FeedItemShortcut> shortcuts, final Set<String> readGuids, final Date lastViewedItemTime) {
        guard(notNull(shortcuts));
        guard(notNull(readGuids));
        guard(notNull(lastViewedItemTime));

        final List<FeedItemShortcut> notReads = findNotReadItems(shortcuts, readGuids);

        Collections.sort(notReads, TIMESTAMP_ASCENDING_COMPARATOR_FOR_SHORTCUT);

        if (!readGuids.isEmpty()) {

            for (final FeedItemShortcut candidate : notReads) {
                final boolean youngerThanLastViewedItem = candidate.date.compareTo(lastViewedItemTime) > 0;

                if (youngerThanLastViewedItem) {
                    return candidate;
                }
            }
        }

        return notReads.isEmpty() ? null : notReads.get(notReads.size() - 1);
    }

    public static FeedItemReport getFeedItemReport(final UUID feedId, final String link, final ReadFeedItems readFeedItems, final FeedItem feedItem, final int index, final int total) {
        guard(isValidFeedHeaderId(feedId));
        guard(notNull(readFeedItems));
        guard(notNull(feedItem));
        guard(isPositive(index));
        guard(isPositive(total));
        guard(isValidUrl(link));

        final boolean readItem = readFeedItems.readItemIds.contains(feedItem.guid);
        final boolean readLaterItem = readFeedItems.readLaterItemIds.contains(feedItem.guid);
        final boolean addedSinceLastView = readFeedItems.lastUpdate.compareTo(feedItem.date) < 0;

        final String originalDescription = feedItem.description;
        final List<ContentElement> elements = transform(getBaseLink(link), originalDescription);
        final String preparedDescription = render(elements);

        return new FeedItemReport(feedId, feedItem.title, preparedDescription, feedItem.gotoLink, feedItem.date, feedItem.guid, readItem, readLaterItem, addedSinceLastView, index, total);
    }

    private static List<FeedItemShortcut> findNotReadItems(List<FeedItemShortcut> shortcuts, Set<String> readGuids) {
        final List<FeedItemShortcut> notReads = new ArrayList<>();

        for (final FeedItemShortcut candidate : shortcuts) {
            final boolean notRead = !readGuids.contains(candidate.guid);

            if (notRead) {
                notReads.add(candidate);
            }
        }
        return notReads;
    }

    private static int countYoungerItems(final List<FeedItemShortcut> shortcuts, final Date lastUpdate) {
        int count = 0;

        for (final FeedItemShortcut shortcut : shortcuts) {

            if (shortcut.date.compareTo(lastUpdate) > 0) {
                ++count;
            }
        }

        return count;
    }

    private static int countReadLaterItems(final List<FeedItemShortcut> shortcuts, final Set<String> readLaterItemIds) {
        int count = 0;

        for (final FeedItemShortcut shortcut : shortcuts) {

            if (readLaterItemIds.contains(shortcut.guid)) {
                ++count;
            }
        }

        return count;
    }

    private static Set<String> getStoredGuids(final List<FeedItem> items) {
        return getStoredGuids(items, 0);
    }

    private static Set<String> getStoredGuidsFromShortcuts(final List<FeedItemShortcut> shortcuts) {
        final Set<String> storedGuids = new HashSet<>();

        for (final FeedItemShortcut shortcut : shortcuts) {
            storedGuids.add(shortcut.guid);
        }

        return storedGuids;
    }


    private static Set<String> getStoredGuids(final List<FeedItem> items, final long beforeTimestamp) {
        final boolean filtered = beforeTimestamp != 0;
        final Date beforeDate = new Date(beforeTimestamp);

        final Set<String> storedGuids = new HashSet<>();

        for (final FeedItem item : items) {
            final boolean canBeAdded = !filtered || (item.date.compareTo(beforeDate) <= 0);

            if (canBeAdded) {
                storedGuids.add(item.guid);
            }
        }

        return storedGuids;
    }

    private static FeedItem findYoungest(final List<FeedItem> items) {

        if (items.isEmpty()) {
            return null;
        }

        FeedItem youngest = items.get(0);

        for (final FeedItem item : items) {

            if (item.date.getTime() > youngest.date.getTime()) {
                youngest = item;
            }
        }

        return youngest;
    }

    private static FeedItem find(final String itemId, final List<FeedItem> items) {
        final List<FeedItem> found = find(new HashSet<String>() {{
            add(itemId);
        }}, items);

        return found.isEmpty() ? null : found.get(0);
    }

    private static List<FeedItem> find(final Set<String> itemIds, final List<FeedItem> items) {
        final List<FeedItem> result = new ArrayList<>();

        for (final FeedItem candidate : items) {

            if (itemIds.contains(candidate.guid)) {
                result.add(candidate);
            }
        }

        return result;
    }

}
