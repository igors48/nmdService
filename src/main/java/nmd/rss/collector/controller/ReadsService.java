package nmd.rss.collector.controller;

import com.google.appengine.api.datastore.Transaction;
import nmd.rss.collector.Transactions;
import nmd.rss.collector.error.ServiceException;
import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.collector.feed.FeedItem;
import nmd.rss.collector.twitter.TwitterClientTools;
import nmd.rss.collector.updater.FeedHeadersRepository;
import nmd.rss.collector.updater.FeedItemsRepository;
import nmd.rss.collector.updater.UrlFetcher;
import nmd.rss.reader.FeedItemsComparisonReport;
import nmd.rss.reader.ReadFeedItems;
import nmd.rss.reader.ReadFeedItemsRepository;

import java.util.*;

import static nmd.rss.collector.feed.FeedHeader.isValidFeedHeaderId;
import static nmd.rss.collector.feed.FeedItem.isValidFeedItemGuid;
import static nmd.rss.collector.feed.TimestampAscendingComparator.TIMESTAMP_ASCENDING_COMPARATOR;
import static nmd.rss.collector.feed.TimestampDescendingComparator.TIMESTAMP_DESCENDING_COMPARATOR;
import static nmd.rss.collector.util.Assert.guard;
import static nmd.rss.collector.util.Parameter.notNull;
import static nmd.rss.collector.util.TransactionTools.rollbackIfActive;
import static nmd.rss.reader.FeedItemsComparator.compare;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 02.02.14
 */
public class ReadsService extends AbstractService {

    private final Transactions transactions;
    private final ReadFeedItemsRepository readFeedItemsRepository;

    public ReadsService(final FeedHeadersRepository feedHeadersRepository, final FeedItemsRepository feedItemsRepository, final ReadFeedItemsRepository readFeedItemsRepository, final UrlFetcher fetcher, final Transactions transactions) {
        super(feedHeadersRepository, feedItemsRepository, fetcher);

        guard(notNull(transactions));
        this.transactions = transactions;

        guard(notNull(readFeedItemsRepository));
        this.readFeedItemsRepository = readFeedItemsRepository;
    }

    public List<FeedReadReport> getFeedsReadReport() {
        Transaction transaction = null;

        try {
            transaction = this.transactions.beginOne();

            final List<FeedHeader> headers = this.feedHeadersRepository.loadHeaders();
            final List<FeedReadReport> report = new ArrayList<>();

            for (final FeedHeader header : headers) {
                final List<FeedItem> items = this.feedItemsRepository.loadItems(header.id);
                final ReadFeedItems readFeedItems = this.readFeedItemsRepository.load(header.id);

                final FeedReadReport feedReadReport = createFeedReadReport(header, items, readFeedItems);

                report.add(feedReadReport);
            }

            transaction.commit();

            return report;
        } finally {
            rollbackIfActive(transaction);
        }
    }

    public FeedItemsReport getFeedItemsReport(final UUID feedId) throws ServiceException {
        guard(isValidFeedHeaderId(feedId));

        Transaction transaction = null;

        try {
            transaction = this.transactions.beginOne();

            final FeedHeader header = loadFeedHeader(feedId);

            final ArrayList<FeedItemReport> feedItemReports = new ArrayList<>();

            final List<FeedItem> feedItems = this.feedItemsRepository.loadItems(feedId);
            Collections.sort(feedItems, TIMESTAMP_DESCENDING_COMPARATOR);

            final ReadFeedItems readFeedItems = this.readFeedItemsRepository.load(feedId);

            int read = 0;
            int notRead = 0;
            int readLater = 0;

            for (final FeedItem feedItem : feedItems) {
                final boolean readItem = readFeedItems.readItemIds.contains(feedItem.guid);
                final boolean readLaterItem = readFeedItems.readLaterItemIds.contains(feedItem.guid);
                final boolean addedSinceLastView = readFeedItems.lastUpdate.compareTo(feedItem.date) < 0;

                final FeedItemReport feedItemReport = new FeedItemReport(feedId, feedItem.title, feedItem.description, feedItem.link, feedItem.date, feedItem.guid, readItem, readLaterItem, addedSinceLastView);
                feedItemReports.add(feedItemReport);

                if (readItem) {
                    ++read;
                } else {
                    ++notRead;
                }

                if (readLaterItem) {
                    ++readLater;
                }
            }

            transaction.commit();

            return new FeedItemsReport(header.id, header.title, read, notRead, readLater, feedItemReports, readFeedItems.lastUpdate);
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

    public void markAllItemsAsRead(final UUID feedId) throws ServiceException {
        guard(isValidFeedHeaderId(feedId));

        Transaction transaction = null;

        try {
            transaction = this.transactions.beginOne();

            loadFeedHeader(feedId);

            final Set<String> readGuids = new HashSet<>();
            final Set<String> readLaterGuids = new HashSet<>();

            final List<FeedItem> items = this.feedItemsRepository.loadItems(feedId);
            final FeedItem youngest = findYoungest(items);
            final Set<String> storedGuids = getStoredGuids(items);
            readGuids.addAll(storedGuids);

            final ReadFeedItems readFeedItems = this.readFeedItemsRepository.load(feedId);
            readLaterGuids.addAll(readFeedItems.readLaterItemIds);

            final Date lastUpdate = youngest == null ? new Date() : youngest.date;
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

    public static FeedReadReport createFeedReadReport(final FeedHeader header, final List<FeedItem> items, final ReadFeedItems readFeedItems) {
        guard(notNull(header));
        guard(notNull(items));
        guard(notNull(readFeedItems));

        final Set<String> storedGuids = getStoredGuids(items);

        final FeedItemsComparisonReport comparisonReport = compare(readFeedItems.readItemIds, storedGuids);

        final FeedItem topItem = findFirstNotReadFeedItem(items, readFeedItems.readItemIds, readFeedItems.lastUpdate);
        final String topItemId = topItem == null ? null : topItem.guid;
        final String topItemLink = topItem == null ? null : topItem.link;

        final int addedFromLastVisit = countYoungerItems(items, readFeedItems.lastUpdate);

        final int readLaterItemsCount = countReadLaterItems(items, readFeedItems.readLaterItemIds);

        final FeedType feedType = TwitterClientTools.isItTwitterUrl(header.feedLink) ? FeedType.TWITTER : FeedType.RSS;

        return new FeedReadReport(header.id, feedType, header.title, comparisonReport.readItems.size(), comparisonReport.newItems.size(), readLaterItemsCount, addedFromLastVisit, topItemId, topItemLink, readFeedItems.lastUpdate);
    }

    public static FeedItem findFirstNotReadFeedItem(final List<FeedItem> items, final Set<String> readGuids, final Date lastViewedItemTime) {
        guard(notNull(items));
        guard(notNull(readGuids));
        guard(notNull(lastViewedItemTime));

        final List<FeedItem> notReads = findNotReadItems(items, readGuids);

        Collections.sort(notReads, TIMESTAMP_ASCENDING_COMPARATOR);

        if (!readGuids.isEmpty()) {

            for (final FeedItem candidate : notReads) {
                final boolean youngerThanLastViewedItem = candidate.date.compareTo(lastViewedItemTime) > 0;

                if (youngerThanLastViewedItem) {
                    return candidate;
                }
            }
        }

        return notReads.isEmpty() ? null : notReads.get(notReads.size() - 1);
    }

    private static List<FeedItem> findNotReadItems(List<FeedItem> items, Set<String> readGuids) {
        final List<FeedItem> notReads = new ArrayList<>();

        for (final FeedItem candidate : items) {
            final boolean notRead = !readGuids.contains(candidate.guid);

            if (notRead) {
                notReads.add(candidate);
            }
        }
        return notReads;
    }

    private static int countYoungerItems(final List<FeedItem> items, final Date lastUpdate) {
        int count = 0;

        for (final FeedItem item : items) {

            if (item.date.compareTo(lastUpdate) > 0) {
                ++count;
            }
        }

        return count;
    }

    private static int countReadLaterItems(final List<FeedItem> items, final Set<String> readLaterItemIds) {
        int count = 0;

        for (final FeedItem item : items) {

            if (readLaterItemIds.contains(item.guid)) {
                ++count;
            }
        }

        return count;
    }

    private static Set<String> getStoredGuids(final List<FeedItem> items) {
        final Set<String> storedGuids = new HashSet<>();

        for (final FeedItem item : items) {
            storedGuids.add(item.guid);
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

    private static FeedItem find(String itemId, List<FeedItem> items) {

        for (final FeedItem candidate : items) {

            if (candidate.guid.equals(itemId)) {
                return candidate;
            }
        }

        return null;
    }

}
