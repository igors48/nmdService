package nmd.orb.collector.feed;

import java.io.Serializable;
import java.util.Comparator;

import static nmd.orb.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 13.05.13
 */
public class TimestampAscendingComparator implements Comparator<FeedItem>, Serializable {

    public static final TimestampAscendingComparator TIMESTAMP_ASCENDING_COMPARATOR = new TimestampAscendingComparator();

    @Override
    public int compare(final FeedItem first, final FeedItem second) {
        assertNotNull(first);
        assertNotNull(second);

        return first.date.compareTo(second.date);
    }

}

