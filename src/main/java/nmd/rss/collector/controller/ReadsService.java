package nmd.rss.collector.controller;

import com.google.appengine.api.datastore.Transaction;
import nmd.rss.collector.Transactions;
import nmd.rss.collector.error.ServiceException;
import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.collector.feed.FeedItem;
import nmd.rss.collector.updater.FeedHeadersRepository;
import nmd.rss.collector.updater.FeedItemsRepository;
import nmd.rss.collector.updater.UrlFetcher;
import nmd.rss.reader.FeedItemsComparisonReport;
import nmd.rss.reader.ReadFeedItems;
import nmd.rss.reader.ReadFeedItemsRepository;

import java.util.*;

import static nmd.rss.collector.feed.TimestampDescendingComparator.TIMESTAMP_DESCENDING_COMPARATOR;
import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.collector.util.Assert.assertStringIsValid;
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

        assertNotNull(transactions);
        this.transactions = transactions;

        assertNotNull(readFeedItemsRepository);
        this.readFeedItemsRepository = readFeedItemsRepository;
    }

    public static FeedItem findLastNotReadFeedItem(final List<FeedItem> items, final Set<String> readGuids) {
        assertNotNull(items);
        assertNotNull(readGuids);

        Collections.sort(items, TIMESTAMP_DESCENDING_COMPARATOR);

        for (final FeedItem candidate : items) {

            if (!readGuids.contains(candidate.guid)) {
                return candidate;
            }
        }

        return null;
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

    public List<FeedReadReport> getFeedsReadReport() {
        Transaction transaction = null;

        try {
            transaction = this.transactions.beginOne();

            final List<FeedHeader> headers = this.feedHeadersRepository.loadHeaders();
            final List<FeedReadReport> report = new ArrayList<>();

            for (final FeedHeader header : headers) {
                final List<FeedItem> items = this.feedItemsRepository.loadItems(header.id);

                final Set<String> storedGuids = getStoredGuids(items);
                final ReadFeedItems readFeedItems = this.readFeedItemsRepository.load(header.id);

                final FeedItemsComparisonReport comparisonReport = compare(readFeedItems.readItemIds, storedGuids);

                final FeedItem topItem = findLastNotReadFeedItem(items, readFeedItems.readItemIds);
                final String topItemId = topItem == null ? null : topItem.guid;
                final String topItemLink = topItem == null ? null : topItem.link;

                final int addedFromLastVisit = countYoungerItems(items, readFeedItems.lastUpdate);
                final FeedReadReport feedReadReport = new FeedReadReport(header.id, header.title, comparisonReport.readItems.size(), comparisonReport.newItems.size(), addedFromLastVisit, topItemId, topItemLink);

                report.add(feedReadReport);
            }

            transaction.commit();

            return report;
        } finally {
            rollbackIfActive(transaction);
        }
    }

    public FeedItemsReport getFeedItemsReport(final UUID feedId) throws ServiceException {
        assertNotNull(feedId);

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

            for (final FeedItem feedItem : feedItems) {
                final boolean readItem = readFeedItems.readItemIds.contains(feedItem.guid);
                final boolean readLaterItem = readFeedItems.readLaterItemIds.contains(feedItem.guid);

                final FeedItemReport feedItemReport = new FeedItemReport(feedId, feedItem.title, feedItem.description, feedItem.link, feedItem.date, feedItem.guid, readItem, readLaterItem);
                feedItemReports.add(feedItemReport);

                if (readItem) {
                    ++read;
                } else {
                    ++notRead;
                }
            }

            transaction.commit();

            return new FeedItemsReport(header.id, header.title, read, notRead, feedItemReports);
        } finally {
            rollbackIfActive(transaction);
        }
    }

    public void toggleReadLaterItemMark(final UUID feedId, final String itemId) throws ServiceException {
        assertNotNull(feedId);
        assertStringIsValid(itemId);

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

                final ReadFeedItems updatedReadFeedItems = new ReadFeedItems(readFeedItems.lastUpdate, readFeedItems.readItemIds, backedReadLaterItemIds, readFeedItems.categoryId);
                this.readFeedItemsRepository.store(feedId, updatedReadFeedItems);
            }

            transaction.commit();
        } finally {
            rollbackIfActive(transaction);
        }
    }

    public void markItemAsRead(final UUID feedId, final String itemId) throws ServiceException {
        assertNotNull(feedId);
        assertStringIsValid(itemId);

        Transaction transaction = null;

        try {
            transaction = this.transactions.beginOne();

            loadFeedHeader(feedId);

            final Set<String> storedGuids = getStoredGuids(feedId);
            final ReadFeedItems readFeedItems = this.readFeedItemsRepository.load(feedId);

            final Set<String> readGuids = new HashSet<>();
            readGuids.addAll(readFeedItems.readItemIds);
            readGuids.add(itemId);

            final Set<String> readLaterGuids = new HashSet<>();
            readLaterGuids.addAll(readFeedItems.readLaterItemIds);
            readLaterGuids.remove(itemId);

            final FeedItemsComparisonReport comparisonReport = compare(readGuids, storedGuids);

            final ReadFeedItems updatedReadFeedItems = new ReadFeedItems(new Date(), comparisonReport.readItems, readLaterGuids, readFeedItems.categoryId);
            this.readFeedItemsRepository.store(feedId, updatedReadFeedItems);

            transaction.commit();
        } finally {
            rollbackIfActive(transaction);
        }
    }

    public void markAllItemsAsRead(final UUID feedId) throws ServiceException {
        assertNotNull(feedId);

        Transaction transaction = null;

        try {
            transaction = this.transactions.beginOne();

            loadFeedHeader(feedId);

            final Set<String> readGuids = new HashSet<>();
            final Set<String> readLaterGuids = new HashSet<>();

            final Set<String> storedGuids = getStoredGuids(feedId);
            readGuids.addAll(storedGuids);

            final ReadFeedItems readFeedItems = this.readFeedItemsRepository.load(feedId);
            readLaterGuids.addAll(readFeedItems.readLaterItemIds);

            final ReadFeedItems updatedReadFeedItems = new ReadFeedItems(new Date(), readGuids, readLaterGuids, readFeedItems.categoryId);
            this.readFeedItemsRepository.store(feedId, updatedReadFeedItems);

            transaction.commit();
        } finally {
            rollbackIfActive(transaction);
        }
    }

    private Set<String> getStoredGuids(final List<FeedItem> items) {
        final Set<String> storedGuids = new HashSet<>();

        for (final FeedItem item : items) {
            storedGuids.add(item.guid);
        }

        return storedGuids;
    }

    private Set<String> getStoredGuids(final UUID feedId) {
        final List<FeedItem> items = this.feedItemsRepository.loadItems(feedId);

        return getStoredGuids(items);
    }

}
