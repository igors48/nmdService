package http.reads;

import http.AbstractHttpTest;
import nmd.orb.collector.error.ErrorCode;
import nmd.orb.http.responses.FeedIdResponse;
import org.junit.Test;

import java.util.UUID;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 27.01.14
 */
public class MarkFeedItemCommonTest extends AbstractHttpTest {

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

        assertErrorResponse(markItem(feedIdResponse.feedId.toString(), "guid", "invalidMarkMode"), ErrorCode.INVALID_MARK_MODE);
    }

}
