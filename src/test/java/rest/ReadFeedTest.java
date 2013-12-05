package rest;

import nmd.rss.collector.error.ErrorCode;
import nmd.rss.collector.rest.responses.FeedIdResponse;
import nmd.rss.collector.rest.responses.FeedReadReportsResponse;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 03.12.13
 */
public class ReadFeedTest extends AbstractRestTest {

    @Test
    public void whenFeedExistsThenReadReportReturns() {
        addFirstFeed();

        final FeedReadReportsResponse response = getReadsReport();

        assertFalse(response.getReports().isEmpty());
    }

    @Test
    public void whenThereAreNoFeedsThenNoReportsReturn() {
        final FeedReadReportsResponse response = getReadsReport();

        assertTrue(response.getReports().isEmpty());
    }

    @Test
    public void whenItemMarkedAsReadInExistsFeedThenSuccessResponseReturns() {
        final FeedIdResponse feedIdResponse = addFirstFeed();

        assertSuccessResponse(markItemAsRead(feedIdResponse.getFeedId().toString(), "guid"));
    }

    @Test
    public void whenItemMarkedAsReadInNotExistsFeedThenErrorReturns() {
        assertErrorResponse(markItemAsRead(UUID.randomUUID().toString(), "guid"), ErrorCode.WRONG_FEED_ID);
    }

    @Test
    public void whenItemIdIsEmptyThenErrorReturns() {
        final FeedIdResponse feedIdResponse = addFirstFeed();

        assertErrorResponse(markItemAsRead(feedIdResponse.getFeedId().toString(), ""), ErrorCode.INVALID_FEED_OR_ITEM_ID);
    }

    @Test
    public void whenItemIdAreSpacesThenErrorReturns() {
        final FeedIdResponse feedIdResponse = addFirstFeed();

        assertErrorResponse(markItemAsRead(feedIdResponse.getFeedId().toString(), "  "), ErrorCode.INVALID_FEED_OR_ITEM_ID);
    }

    @Test
    public void whenFeedIdIsNotValidThenErrorReturns() {
        assertErrorResponse(markItemAsRead("12345678", "guid"), ErrorCode.INVALID_FEED_OR_ITEM_ID);
    }

    @Test
    public void whenFeedIdIsEmptyValidThenErrorReturns() {
        assertErrorResponse(markItemAsRead("", "guid"), ErrorCode.INVALID_FEED_OR_ITEM_ID);
    }

}
