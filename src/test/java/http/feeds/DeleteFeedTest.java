package http.feeds;

import http.AbstractHttpTest;
import nmd.orb.error.ErrorCode;
import nmd.orb.http.responses.FeedIdResponse;
import org.junit.Test;

import java.util.UUID;

/**
 * User: igu
 * Date: 03.12.13
 */
public class DeleteFeedTest extends AbstractHttpTest {

    @Test
    public void whenFeedWithExistentIdDeletedThenSuccessResponseReturns() {
        final FeedIdResponse feedIdResponse = addFirstFeed();

        final String response = deleteFeed(feedIdResponse.feedId.toString());

        assertSuccessResponse(response);
    }

    @Test
    public void whenTryToDeleteFeedWithNoIdThenErrorReturns() {
        final String response = deleteFeed("");

        assertErrorResponse(response, ErrorCode.INVALID_FEED_ID);
    }

    @Test
    public void whenTryToDeleteFeedWithIncorrectIdThenErrorReturns() {
        final String response = deleteFeed("qwerty");

        assertErrorResponse(response, ErrorCode.INVALID_FEED_ID);
    }

    @Test
    public void whenTryToDeleteFeedThatNotExistThenSuccessResponseReturns() {
        final String response = deleteFeed(UUID.randomUUID().toString());

        assertSuccessResponse(response);
    }

}
