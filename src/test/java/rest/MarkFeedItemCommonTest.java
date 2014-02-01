package rest;

import nmd.rss.collector.error.ErrorCode;
import nmd.rss.collector.rest.responses.FeedIdResponse;
import org.junit.Test;

import java.util.UUID;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 27.01.14
 */
public class MarkFeedItemCommonTest extends AbstractRestTest {

    @Test
    public void whenMarkedItemNotExistsInFeedThenErrorReturns() {
        assertErrorResponse(markItemAsRead(UUID.randomUUID().toString(), "guid"), ErrorCode.WRONG_FEED_ID);
    }

    @Test
    public void whenFeedIdIsNotValidThenErrorReturns() {
        assertErrorResponse(markItemAsRead("12345678", "guid"), ErrorCode.INVALID_FEED_OR_ITEM_ID);
    }

    @Test
    public void whenFeedIdIsEmptyThenErrorReturns() {
        assertErrorResponse(markItemAsRead("", "guid"), ErrorCode.INVALID_FEED_OR_ITEM_ID);
    }

    @Test
    public void whenMarkModeIsInvalidThenErrorReturns() {
        final FeedIdResponse feedIdResponse = addFirstFeed();

        assertErrorResponse(markItem(feedIdResponse.getFeedId().toString(), "guid", "invalidMarkMode"), ErrorCode.INVALID_MARK_MODE);
    }

}
