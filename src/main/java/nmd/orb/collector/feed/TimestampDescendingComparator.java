package nmd.orb.collector.feed;

import java.io.Serializable;
import java.util.Comparator;

import static nmd.orb.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 13.05.13
 */
public class TimestampDescendingComparator implements Comparator<FeedItem>, Serializable {

    public static final TimestampDescendingComparator TIMESTAMP_DESCENDING_COMPARATOR = new TimestampDescendingComparator();

    @Override
    public int compare(final FeedItem first, final FeedItem second) {
        assertNotNull(first);
        assertNotNull(second);

        return -(first.date.compareTo(second.date));
    }

}

