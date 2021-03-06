package http.feeds;

import http.AbstractHttpTest;
import nmd.orb.error.ErrorCode;
import org.junit.Test;

import java.util.UUID;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date: 09.01.14
 */
public class UpdateFeedTitleTest extends AbstractHttpTest {

    @Test
    public void whenFeedIdAndNewTitleAreValidThenSuccessReturns() {
        final UUID feedId = addFirstFeed().feedId;

        final String response = updateFeedTitle(feedId.toString(), "updated_title");

        assertSuccessResponse(response);
    }

    @Test
    public void whenFeedIdIsUnknownThenErrorReturns() {
        final String response = updateFeedTitle(UUID.randomUUID().toString(), "updated_title");

        assertErrorResponse(response, ErrorCode.WRONG_FEED_ID);
    }

    @Test
    public void whenFeedIdIsInvalidThenErrorReturns() {
        final String response = updateFeedTitle("invalid", "updated_title");

        assertErrorResponse(response, ErrorCode.INVALID_FEED_ID);
    }

    @Test
    public void whenTitleIsEmptyThenErrorReturns() {
        final UUID feedId = addFirstFeed().feedId;

        final String response = updateFeedTitle(feedId.toString(), "");

        assertErrorResponse(response, ErrorCode.INVALID_FEED_TITLE);
    }

    @Test
    public void whenTitleContainsSpacesOnlyThenErrorReturns() {
        final UUID feedId = addFirstFeed().feedId;

        final String response = updateFeedTitle(feedId.toString(), "    ");

        assertErrorResponse(response, ErrorCode.INVALID_FEED_TITLE);
    }

}
