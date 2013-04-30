package nmd.rss.collector;

import java.util.*;

import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.collector.util.Assert.assertPositive;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 29.04.13
 */
public final class FeedItemsMerger {

    private static final TimestampComparator TIMESTAMP_COMPARATOR = new TimestampComparator();

    public static FeedItemsMergeReport merge(final List<FeedItem> olds, final List<FeedItem> youngs, final int maximumCount) {
        assertNotNull(olds);
        assertNotNull(youngs);
        assertPositive(maximumCount);

        List<FeedItem> removed = new ArrayList<>();
        List<FeedItem> retained = new ArrayList<>();
        List<FeedItem> added = new ArrayList<>();

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

        int delta = maximumCount - totalCount;
        removeOldest(retained, removed, delta);

        totalCount = retained.size() + added.size();

        if (totalCount <= maximumCount) {
            return;
        }

        delta = maximumCount - totalCount;
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
            FeedItem victim = source.remove(index);
            removed.add(victim);
        }
    }

    private static void sortByTimestamp(final List<FeedItem> source) {
        Collections.sort(source, TIMESTAMP_COMPARATOR);
    }

    private static void removePossibleDuplicates(final List<FeedItem> olds, final List<FeedItem> youngs, final List<FeedItem> removed, final List<FeedItem> retained, final List<FeedItem> added) {
        Map<String, FeedItem> oldItems = createUniqueFeedItemsLinkMap(olds);
        Map<String, FeedItem> youngItems = createUniqueFeedItemsLinkMap(youngs);

        for (String link : youngItems.keySet()) {
            FeedItem oldItem = oldItems.get(link);

            if (oldItem != null) {
                removed.add(oldItem);
                oldItems.remove(link);
            }

            added.add(youngItems.get(link));
        }

        retained.addAll(oldItems.values());
    }

    private static Map<String, FeedItem> createUniqueFeedItemsLinkMap(final List<FeedItem> feedItems) {
        Map<String, FeedItem> result = new HashMap<>();

        for (FeedItem current : feedItems) {
            result.put(current.link, current);
        }

        return result;
    }

    private static class TimestampComparator implements Comparator<FeedItem> {

        @Override
        public int compare(FeedItem first, FeedItem second) {
            return (int) (first.timestamp - second.timestamp);
        }
    }

    private FeedItemsMerger() {
        // private
    }

}
