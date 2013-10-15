package nmd.rss.reader;

import java.util.HashSet;
import java.util.Set;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 07.09.13
 */
public class FeedItemsComparator {

    public static FeedItemsComparisonReport findNotReadItems(final Set<String> readItems, final Set<String> storedItems) {
        assertNotNull(readItems);
        assertNotNull(storedItems);

        final Set<String> reads = new HashSet<>();
        final Set<String> notReads = new HashSet<>();

        for (final String current : readItems) {

            if (storedItems.contains(current)) {
                reads.add(current);
            } else {
                notReads.add(current);
            }
        }

        return new FeedItemsComparisonReport(reads, notReads);
    }

    private FeedItemsComparator() {
        // empty
    }

}
