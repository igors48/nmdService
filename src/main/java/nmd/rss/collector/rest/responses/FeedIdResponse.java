package nmd.rss.collector.rest.responses;

import java.util.UUID;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.06.13
 */
public class FeedIdResponse extends SuccessResponse {

    private UUID feedId = null;

    private FeedIdResponse() {
        // empty
    }

    public static FeedIdResponse create(final UUID feedId) {
        assertNotNull(feedId);

        final FeedIdResponse result = new FeedIdResponse();

        result.feedId = feedId;

        return result;
    }

}
