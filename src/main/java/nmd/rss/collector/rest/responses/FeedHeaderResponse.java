package nmd.rss.collector.rest.responses;

import nmd.rss.collector.feed.FeedHeader;

import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.collector.util.Assert.assertStringIsValid;

/**
 * User: igu
 * Date: 14.01.14
 */
public class FeedHeaderResponse extends SuccessResponse {

    private String feedLink = null;
    private String feedId = null;
    private String feedTitle = null;

    private FeedHeaderResponse() {
        // empty
    }

    public FeedHeaderResponse(final String feedLink, final String feedId, final String feedTitle) {
        assertStringIsValid(feedLink);
        this.feedLink = feedLink;

        assertStringIsValid(feedId);
        this.feedId = feedId;

        assertStringIsValid(feedTitle);
        this.feedTitle = feedTitle;
    }

    public static FeedHeaderResponse convert(final FeedHeader header) {
        assertNotNull(header);

        final FeedHeaderResponse result = new FeedHeaderResponse();

        result.feedId = header.id.toString();
        result.feedLink = header.feedLink;
        result.feedTitle = header.title;

        return result;
    }

}
