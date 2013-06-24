package nmd.rss.collector.rest;

import nmd.rss.collector.feed.FeedHeader;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.06.13
 */
class FeedHeaderHelper {

    private String feedLink;
    private String feedId;

    private FeedHeaderHelper() {
        this.feedLink = null;
        this.feedId = null;
    }

    String getFeedLink() {
        return this.feedLink;
    }

    void setFeedLink(final String feedLink) {
        this.feedLink = feedLink;
    }

    String getFeedId() {
        return this.feedId;
    }

    void setFeedId(final String feedId) {
        this.feedId = feedId;
    }

    static FeedHeaderHelper convert(final FeedHeader header) {
        assertNotNull(header);

        final FeedHeaderHelper result = new FeedHeaderHelper();
        result.setFeedId(header.id.toString());
        result.setFeedLink(header.feedLink);

        return result;
    }

}
