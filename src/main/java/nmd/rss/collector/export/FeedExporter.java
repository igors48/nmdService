package nmd.rss.collector.export;

import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.collector.feed.FeedItem;

import java.util.List;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 16.05.13
 */
public final class FeedExporter {

    public static String export(final FeedHeader header, final List<FeedItem> items) {
        assertNotNull(header);
        assertNotNull(items);

        return "";
    }

    private FeedExporter() {
        // empty
    }

}
