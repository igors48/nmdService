package nmd.rss.collector.rest.responses.payload;

import nmd.rss.collector.feed.FeedHeader;

import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.collector.util.Assert.assertStringIsValid;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.06.13
 */
public class FeedHeaderPayload {

    public String feedLink = null;
    public String feedId = null;
    public String feedTitle = null;

    private FeedHeaderPayload() {
        // empty
    }

    public static FeedHeaderPayload create(final String feedLink, final String feedId, final String feedTitle) {
        assertStringIsValid(feedLink);
        assertStringIsValid(feedId);
        assertStringIsValid(feedTitle);

        final FeedHeaderPayload feedHeaderPayload = new FeedHeaderPayload();

        feedHeaderPayload.feedLink = feedLink;
        feedHeaderPayload.feedId = feedId;
        feedHeaderPayload.feedTitle = feedTitle;

        return feedHeaderPayload;
    }

    public static FeedHeaderPayload create(final FeedHeader header) {
        assertNotNull(header);

        final FeedHeaderPayload result = new FeedHeaderPayload();

        result.feedId = header.id.toString();
        result.feedLink = header.feedLink;
        result.feedTitle = header.title;

        return result;
    }

}
