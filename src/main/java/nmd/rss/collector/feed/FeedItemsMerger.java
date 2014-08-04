package nmd.rss.collector.feed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static nmd.rss.collector.feed.TimestampAscendingComparator.TIMESTAMP_ASCENDING_COMPARATOR;
import static nmd.rss.collector.feed.TimestampDescendingComparator.TIMESTAMP_DESCENDING_COMPARATOR;
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

        final List<FeedItem> youngsSorted = new ArrayList<>(youngs.size());
        youngsSorted.addAll(youngs);
        sortFromNewToOld(youngsSorted);

        final List<FeedItem> oldsSorted = new ArrayList<>(olds.size());
        oldsSorted.addAll(olds);
        sortFromNewToOld(oldsSorted);

        final Iterator<FeedItem> youngItemsIterator = youngsSorted.iterator();

        while (youngItemsIterator.hasNext() && added.size() + retained.size() < maximumCount) {
            final FeedItem youngItem = youngItemsIterator.next();
            youngItemsIterator.remove();

            final List<FeedItem> existent = findByLink(youngItem.link, oldsSorted);
            sortFromNewToOld(existent);

            if (existent.isEmpty()) {
                added.add(youngItem);
            } else {
                final FeedItem youngestExistent = existent.get(0);

                final boolean realDates = youngestExistent.dateReal && youngItem.dateReal;

                if ((youngestExistent.date.compareTo(youngItem.date) >= 0 || !realDates)) {
                    retained.add(youngestExistent);
                    oldsSorted.removeAll(existent);

                    existent.remove(youngestExistent);
                    removed.addAll(existent);
                } else {
                    added.add(youngItem);

                    removed.addAll(existent);
                    oldsSorted.removeAll(existent);
                }
            }
        }

        final Iterator<FeedItem> oldItemsIterator = oldsSorted.iterator();

        while (oldItemsIterator.hasNext() && added.size() + retained.size() < maximumCount) {
            final FeedItem oldItem = oldItemsIterator.next();
            oldItemsIterator.remove();
            retained.add(oldItem);
        }

        removed.addAll(oldsSorted);

        sortFromOldToNew(removed);
        sortFromOldToNew(retained);
        sortFromOldToNew(added);

        return new FeedItemsMergeReport(removed, retained, added);
    }

    public static boolean listEqualsIgnoringGuid(final List<FeedItem> first, final List<FeedItem> second) {
        assertNotNull(first);
        assertNotNull(second);

        if (first.size() != second.size()) {
            return false;
        }

        final List<FeedItem> secondCopy = new ArrayList<>(second);

        for (final FeedItem candidate : first) {

            final int index = find(candidate, secondCopy);

            if (index == -1) {
                return false;
            }

            secondCopy.remove(index);
        }

        return true;
    }

    private static int find(final FeedItem item, final List<FeedItem> list) {
        int index = 0;

        for (final FeedItem candidate : list) {

            if (item.equalsExcludeGuid(candidate)) {
                return index;
            }

            ++index;
        }

        return -1;
    }

    private static List<FeedItem> findByLink(final String link, final List<FeedItem> candidates) {
        final List<FeedItem> result = new ArrayList<>();

        for (final FeedItem candidate : candidates) {

            if (candidate.link.equalsIgnoreCase(link)) {
                result.add(candidate);
            }
        }

        return result;
    }

    private static void sortFromNewToOld(final List<FeedItem> source) {
        Collections.sort(source, TIMESTAMP_DESCENDING_COMPARATOR);
    }

    private static void sortFromOldToNew(final List<FeedItem> source) {
        Collections.sort(source, TIMESTAMP_ASCENDING_COMPARATOR);
    }

    private FeedItemsMerger() {
        // private
    }

}
