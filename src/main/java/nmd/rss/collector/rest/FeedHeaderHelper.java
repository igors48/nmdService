package nmd.rss.collector.rest;

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
}
