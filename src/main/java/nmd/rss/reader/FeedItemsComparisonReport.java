package nmd.rss.reader;

import java.util.Set;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 08.09.13
 */
public class FeedItemsComparisonReport {

    public final Set<String> readItems;
    public final Set<String> newItems;

    public FeedItemsComparisonReport(final Set<String> readItems, final Set<String> newItems) {
        assertNotNull(readItems);
        this.readItems = readItems;

        assertNotNull(newItems);
        this.newItems = newItems;
    }

}
