package nmd.rss.collector.rest;

import java.util.UUID;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.06.13
 */
class FeedIdResponse extends SuccessResponse {

    private UUID feedId;

    private FeedIdResponse() {
    }

    public FeedIdResponse(final UUID feedId) {
        super();

        setFeedId(feedId);
    }

    public UUID getFeedId() {
        return this.feedId;
    }

    public void setFeedId(final UUID feedId) {
        this.feedId = feedId;
    }

}
