package nmd.rss.reader;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 07.09.13
 */
public class FeedItemsComparator {

    public static FeedItemsComparisonReport findNotReadItems(final List<String> readItems, final List<String> storedItems) {
        assertNotNull(readItems);
        assertNotNull(storedItems);

        final Set<String> reads = new HashSet<>();
        final Set<String> notReads = new HashSet<>();

        for (final String readItem : readItems) {

            if (storedItems.contains(readItem)) {
                reads.add(readItem);
            }
        }

        for (final String storedItem : storedItems) {

            if (!readItems.contains(storedItem)) {
                notReads.add(storedItem);
            }
        }

        return new FeedItemsComparisonReport(reads, notReads);
    }

    private FeedItemsComparator() {
        // empty
    }

}
