package nmd.rss.collector.feed;

import java.io.Serializable;
import java.util.Comparator;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 13.05.13
 */
public class TimestampComparator implements Comparator<FeedItem>, Serializable {

    public static final TimestampComparator TIMESTAMP_COMPARATOR = new TimestampComparator();

    @Override
    public int compare(final FeedItem first, final FeedItem second) {
        assertNotNull(first);
        assertNotNull(second);

        return first.date.compareTo(second.date);
    }

}

