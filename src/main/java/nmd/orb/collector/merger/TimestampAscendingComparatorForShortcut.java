package nmd.orb.collector.merger;

import nmd.orb.feed.FeedItemShortcut;

import java.io.Serializable;
import java.util.Comparator;

import static nmd.orb.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 13.05.13
 */
public class TimestampAscendingComparatorForShortcut implements Comparator<FeedItemShortcut>, Serializable {

    public static final TimestampAscendingComparatorForShortcut TIMESTAMP_ASCENDING_COMPARATOR_FOR_SHORTCUT = new TimestampAscendingComparatorForShortcut();

    @Override
    public int compare(final FeedItemShortcut first, final FeedItemShortcut second) {
        assertNotNull(first);
        assertNotNull(second);

        return first.date.compareTo(second.date);
    }

}

