package rest.reads;

import nmd.rss.collector.error.ErrorCode;
import nmd.rss.http.responses.FeedIdResponse;
import nmd.rss.http.responses.FeedItemsCardsReportResponse;
import org.junit.Test;
import rest.AbstractRestTest;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 03.12.13
 */
public class ReadFeedCardsTest extends AbstractRestTest {

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
