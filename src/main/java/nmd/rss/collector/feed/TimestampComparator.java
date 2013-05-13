package nmd.rss.collector.feed;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 13.05.13
 */
public class TimestampComparator implements Comparator<FeedItem>, Serializable {

    public static final TimestampComparator TIMESTAMP_COMPARATOR = new TimestampComparator();

    @Override
    public int compare(final FeedItem first, final FeedItem second) {
        return (int) (first.timestamp - second.timestamp);
    }

}

