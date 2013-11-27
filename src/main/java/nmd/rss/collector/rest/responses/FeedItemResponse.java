package nmd.rss.collector.rest.responses;

import nmd.rss.collector.feed.FeedItem;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 27.11.13
 */
public class FeedItemResponse extends SuccessResponse {

    private String link = "";

    private FeedItemResponse() {
        // empty
    }

    public static FeedItemResponse convert(final FeedItem feedItem) {
        assertNotNull(feedItem);

        final FeedItemResponse feedItemResponse = new FeedItemResponse();
        feedItemResponse.link = feedItem.link;

        return feedItemResponse;
    }

}
