package nmd.rss.collector;

import java.util.List;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 28.04.13
 */
public class Feed {

    private final FeedHeader header;
    private final List<FeedItem> items;

    public Feed(final FeedHeader header, final List<FeedItem> items) {
        assertNotNull(header);
        this.header = header;

        assertNotNull(items);
        this.items = items;
    }

    public final FeedHeader getHeader() {
        return this.header;
    }

    public final List<FeedItem> getItems() {
        return this.items;
    }

}
