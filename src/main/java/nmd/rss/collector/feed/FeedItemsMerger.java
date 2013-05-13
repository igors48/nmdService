package nmd.rss.collector.feed;

import java.util.*;

import static nmd.rss.collector.feed.TimestampComparator.TIMESTAMP_COMPARATOR;
import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.collector.util.Assert.assertPositive;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 29.04.13
 */
public final class FeedItemsMerger {

    public static FeedItemsMergeReport merge(final List<FeedItem> olds, final List<FeedItem> youngs, final int maximumCount) {
        assertNotNull(olds);
        assertNotNull(youngs);
        assertPositive(maximumCount);

        final List<FeedItem> removed = new ArrayList<>();
        final List<FeedItem> retained = new ArrayList<>();
        final List<FeedItem> added = new ArrayList<>();

        removePossibleDuplicates(olds, youngs, removed, retained, added);
        checkMaximumCount(removed, retained, added, maximumCount);

        sortByTimestamp(removed);
        sortByTimestamp(retained);
        sortByTimestamp(added);

        return new FeedItemsMergeReport(removed, retained, added);
    }

    private static void checkMaximumCount(final List<FeedItem> removed, final List<FeedItem> retained, final List<FeedItem> added, final int maximumCount) {
        int totalCount = retained.size() + added.size();

        if (totalCount <= maximumCount) {
            return;
        }

        int delta = totalCount - maximumCount;
        removeOldest(retained, removed, delta);

        totalCount = retained.size() + added.size();

        if (totalCount <= maximumCount) {
            return;
        }

        delta = totalCount - maximumCount;
        removeOldest(added, new ArrayList<FeedItem>(), delta);
    }

    private static void removeOldest(final List<FeedItem> source, final List<FeedItem> removed, final int count) {

        if (source.size() <= count) {
            removed.addAll(source);
            source.clear();

            return;
        }

        sortByTimestamp(source);

        for (int index = 0; index < count; ++index) {
            final FeedItem victim = source.remove(0);
            removed.add(victim);
        }
    }

    private static void sortByTimestamp(final List<FeedItem> source) {
        Collections.sort(source, TIMESTAMP_COMPARATOR);
    }

    private static void removePossibleDuplicates(final List<FeedItem> olds, final List<FeedItem> youngs, final List<FeedItem> removed, final List<FeedItem> retained, final List<FeedItem> added) {
        final Map<String, FeedItem> oldItems = createUniqueFeedItemsLinkMap(olds);
        final Map<String, FeedItem> youngItems = createUniqueFeedItemsLinkMap(youngs);

        for (final String link : youngItems.keySet()) {
            final FeedItem youngItem = youngItems.get(link);
            final FeedItem oldItem = oldItems.get(link);

            if (oldItem == null) {
                added.add(youngItem);
            } else {

                if (!youngItem.sameAs(oldItem)) {
                    removed.add(oldItem);
                    oldItems.remove(link);
                    added.add(youngItem);
                }
            }
        }

        retained.addAll(oldItems.values());
    }

    private static Map<String, FeedItem> createUniqueFeedItemsLinkMap(final List<FeedItem> feedItems) {
        final Map<String, FeedItem> result = new HashMap<>();

        for (final FeedItem current : feedItems) {
            result.put(current.link, current);
        }

        return result;
    }

    private FeedItemsMerger() {
        // private
    }

}
