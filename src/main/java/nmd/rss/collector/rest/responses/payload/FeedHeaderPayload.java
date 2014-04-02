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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FeedHeaderPayload that = (FeedHeaderPayload) o;

        if (feedId != null ? !feedId.equals(that.feedId) : that.feedId != null) return false;
        if (feedLink != null ? !feedLink.equals(that.feedLink) : that.feedLink != null) return false;
        if (feedTitle != null ? !feedTitle.equals(that.feedTitle) : that.feedTitle != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = feedLink != null ? feedLink.hashCode() : 0;
        result = 31 * result + (feedId != null ? feedId.hashCode() : 0);
        result = 31 * result + (feedTitle != null ? feedTitle.hashCode() : 0);
        return result;
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
