package rest;

import nmd.rss.collector.error.ErrorCode;
import nmd.rss.collector.rest.responses.FeedIdResponse;
import org.junit.Test;

import java.util.UUID;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 26.01.14
 */
public class MarkFeedItemAsReadLaterTest extends AbstractRestTest {

    @Test
    public void whenItemMarkedAsReadInExistsFeedThenSuccessResponseReturns() {
        final FeedIdResponse feedIdResponse = addFirstFeed();

        assertSuccessResponse(markItemAsReadLater(feedIdResponse.getFeedId().toString(), "guid"));
    }

    @Test
    public void whenItemMarkedAsReadInNotExistsFeedThenErrorReturns() {
        assertErrorResponse(markItemAsReadLater(UUID.randomUUID().toString(), "guid"), ErrorCode.WRONG_FEED_ID);
    }

    @Test
    public void whenItemIdIsEmptyThenErrorReturns() {
        final FeedIdResponse feedIdResponse = addFirstFeed();

        assertErrorResponse(markItemAsReadLater(feedIdResponse.getFeedId().toString(), ""), ErrorCode.INVALID_FEED_OR_ITEM_ID);
    }

    @Test
    public void whenItemIdAreSpacesThenErrorReturns() {
        final FeedIdResponse feedIdResponse = addFirstFeed();

        assertErrorResponse(markItemAsReadLater(feedIdResponse.getFeedId().toString(), "  "), ErrorCode.INVALID_FEED_OR_ITEM_ID);
    }

    @Test
    public void whenFeedIdIsNotValidThenErrorReturns() {
        assertErrorResponse(markItemAsReadLater("12345678", "guid"), ErrorCode.INVALID_FEED_OR_ITEM_ID);
    }

    @Test
    public void whenFeedIdIsEmptyThenErrorReturns() {
        assertErrorResponse(markItemAsReadLater("", "guid"), ErrorCode.INVALID_FEED_OR_ITEM_ID);
    }

}
