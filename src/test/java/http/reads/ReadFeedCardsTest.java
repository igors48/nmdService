package http.reads;

import http.AbstractHttpTest;
import nmd.orb.collector.error.ErrorCode;
import nmd.orb.http.responses.FeedIdResponse;
import nmd.orb.http.responses.FeedItemsCardsReportResponse;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 03.12.13
 */
public class ReadFeedCardsTest extends AbstractHttpTest {

    @Test
    public void whenFeedIdIsNotValidThenErrorReturns() {
        addFirstFeed();

        final String response = getReadsCardsReportAsString("trash", "1", "2");

        assertErrorResponse(response, ErrorCode.INVALID_FEED_ID);
    }

    @Test
    public void whenFeedIdIsNotFoundThenErrorReturns() {
        addFirstFeed();

        final String response = getReadsCardsReportAsString(UUID.randomUUID().toString(), "1", "2");

        assertErrorResponse(response, ErrorCode.WRONG_FEED_ID);
    }

    @Test
    public void whenOffsetIsNotSpecifiedThenErrorReturns() {
        final FeedIdResponse feedIdResponse = addFirstFeed();

        final String response = getReadsCardsReportAsString(feedIdResponse.feedId.toString(), "1", "");

        assertErrorResponse(response, ErrorCode.INVALID_OFFSET_OR_SIZE);
    }

    @Test
    public void whenOffsetIsNotIntegerThenErrorReturns() {
        final FeedIdResponse feedIdResponse = addFirstFeed();

        final String response = getReadsCardsReportAsString(feedIdResponse.feedId.toString(), "1", "a");

        assertErrorResponse(response, ErrorCode.INVALID_OFFSET_OR_SIZE);
    }

    @Test
    public void whenOffsetIsNegativeIntegerThenErrorReturns() {
        final FeedIdResponse feedIdResponse = addFirstFeed();

        final String response = getReadsCardsReportAsString(feedIdResponse.feedId.toString(), "1", "-1");

        assertErrorResponse(response, ErrorCode.INVALID_OFFSET_OR_SIZE);
    }

    @Test
    public void whenSizeIsNotSpecifiedThenErrorReturns() {
        final FeedIdResponse feedIdResponse = addFirstFeed();

        final String response = getReadsCardsReportAsString(feedIdResponse.feedId.toString(), "", "1");

        assertErrorResponse(response, ErrorCode.INVALID_OFFSET_OR_SIZE);
    }

    @Test
    public void whenSizeIsNotIntegerThenErrorReturns() {
        final FeedIdResponse feedIdResponse = addFirstFeed();

        final String response = getReadsCardsReportAsString(feedIdResponse.feedId.toString(), "a", "1");

        assertErrorResponse(response, ErrorCode.INVALID_OFFSET_OR_SIZE);
    }

    @Test
    public void whenSizeIsNegativeIntegerThenErrorReturns() {
        final FeedIdResponse feedIdResponse = addFirstFeed();

        final String response = getReadsCardsReportAsString(feedIdResponse.feedId.toString(), "-1", "1");

        assertErrorResponse(response, ErrorCode.INVALID_OFFSET_OR_SIZE);
    }

    @Test
    public void whenAllParametersValidThenReportReturns() {
        final FeedIdResponse feedIdResponse = addFirstFeed();

        final FeedItemsCardsReportResponse response = getReadsCardsReport(feedIdResponse.feedId.toString(), "0", "4");

        assertEquals(4, response.reports.size());
    }


}
