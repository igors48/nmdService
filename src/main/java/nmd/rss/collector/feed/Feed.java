package nmd.rss.collector.feed;

import java.util.Collections;
import java.util.List;

import static nmd.rss.collector.util.Assert.guard;
import static nmd.rss.collector.util.Parameter.notNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 28.04.13
 */
public class Feed {

    public final FeedHeader header;
    public final List<FeedItem> items;

    public Feed(final FeedHeader header, final List<FeedItem> items) {
        guard(notNull(header));
        this.header = header;

        guard(notNull(items));
        this.items = Collections.unmodifiableList(items);
    }

}
