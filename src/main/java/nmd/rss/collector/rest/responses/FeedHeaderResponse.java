package nmd.rss.collector.rest.responses;

import nmd.rss.collector.feed.FeedHeader;

import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.collector.util.Assert.assertStringIsValid;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.06.13
 */
public class FeedHeaderResponse {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FeedHeaderResponse that = (FeedHeaderResponse) o;

        if (!feedId.equals(that.feedId)) return false;
        if (!feedLink.equals(that.feedLink)) return false;
        if (!feedTitle.equals(that.feedTitle)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = feedLink.hashCode();

        result = 31 * result + feedId.hashCode();
        result = 31 * result + feedTitle.hashCode();

        return result;
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
