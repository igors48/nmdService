package nmd.rss.collector.rest.responses;

import nmd.rss.collector.feed.FeedHeader;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.06.13
 */
public class FeedHeaderHelper {

    private String feedLink = null;
    private String feedId = null;

    private FeedHeaderHelper() {
        // empty
    }

    public static FeedHeaderHelper convert(final FeedHeader header) {
        assertNotNull(header);

        final FeedHeaderHelper result = new FeedHeaderHelper();

        result.feedId = header.id.toString();
        result.feedLink = header.feedLink;

        return result;
    }

}
