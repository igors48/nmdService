package nmd.orb.reader;

import java.util.HashSet;
import java.util.Set;

import static nmd.orb.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 07.09.13
 */
public class FeedItemsComparator {

    public static FeedItemsComparisonReport compare(final Set<String> readItems, final Set<String> storedItems) {
        assertNotNull(readItems);
        assertNotNull(storedItems);

        final Set<String> reads = new HashSet<>(readItems);
        reads.retainAll(storedItems);

        final Set<String> notReads = new HashSet<>(storedItems);
        notReads.removeAll(reads);

        return new FeedItemsComparisonReport(reads, notReads);
    }

    private FeedItemsComparator() {
        // empty
    }

}
