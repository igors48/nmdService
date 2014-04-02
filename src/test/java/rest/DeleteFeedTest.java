package rest;

import nmd.rss.collector.error.ErrorCode;
import nmd.rss.collector.rest.responses.FeedHeadersResponse;
import nmd.rss.collector.rest.responses.FeedIdResponse;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertTrue;

/**
 * User: igu
 * Date: 03.12.13
 */
public class DeleteFeedTest extends AbstractRestTest {

    @Test
    public void whenFeedWithExistentIdDeletedThenSuccessResponseReturns() {
        final FeedIdResponse feedIdResponse = addFirstFeed();

        final String response = deleteFeed(feedIdResponse.getFeedId().toString());

        assertSuccessResponse(response);
    }

    @Test
    public void whenFeedWithExistentIdDeletedThenItDoesNotReturnInList() {
        final FeedIdResponse feedIdResponse = addFirstFeed();

        deleteFeed(feedIdResponse.getFeedId().toString());

        final FeedHeadersResponse feedsResponse = getFeedHeaders();

        assertTrue(feedsResponse.getPayload().isEmpty());
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
