package rest;

import nmd.rss.collector.error.ErrorCode;
import org.junit.Test;

import java.util.UUID;

/**
 * User: igu
 * Date: 09.01.14
 */
public class UpdateFeedTitleTest extends AbstractRestTest {

    @Test
    public void whenFeedIdAndNewTitleAreValidThenSuccessReturns() {
        final UUID feedId = addFirstFeed().getFeedId();

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
        final UUID feedId = addFirstFeed().getFeedId();

        final String response = updateFeedTitle(feedId.toString(), "");

        assertErrorResponse(response, ErrorCode.INVALID_FEED_TITLE);
    }

    @Test
    public void whenTitleContainsSpacesOnlyThenErrorReturns() {
        final UUID feedId = addFirstFeed().getFeedId();

        final String response = updateFeedTitle(feedId.toString(), "    ");

        assertErrorResponse(response, ErrorCode.INVALID_FEED_TITLE);
    }

}
