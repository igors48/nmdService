package rest;

import nmd.rss.collector.rest.responses.FeedIdResponse;
import nmd.rss.collector.rest.responses.FeedMergeReportResponse;
import nmd.rss.collector.rest.responses.FeedSeriesUpdateResponse;
import org.junit.Test;

import static java.util.UUID.randomUUID;
import static nmd.rss.collector.error.ErrorCode.INVALID_FEED_ID;
import static nmd.rss.collector.error.ErrorCode.WRONG_FEED_ID;
import static org.junit.Assert.assertEquals;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 01.12.13
 */
public class FeedUpdateTest extends AbstractRestTest {

    @Test
    public void whenCurrentFeedExistsThenItUpdatesAndReportReturns() {
        final FeedIdResponse feedIdResponse = addFirstFeed();

        final FeedSeriesUpdateResponse report = updateCurrentFeedWithReport();

        assertEquals(0, report.getErrors().size());
        assertEquals(1, report.getUpdates().size());
    }

    @Test
    public void whenFeedIdExistsThenFeedUpdatesAndReportReturns() {
        final FeedIdResponse feedIdResponse = addFirstFeed();

        final FeedMergeReportResponse report = updateFeedWithReport(feedIdResponse.getFeedId().toString());

        final FeedMergeReportResponse expected = new FeedMergeReportResponse(FIRST_FEED_URL, feedIdResponse.getFeedId(), 0, 100, 0);

        assertEquals(expected, report);
    }

    @Test
    public void whenFeedIdDoesNotExistThenErrorReturns() {
        final String response = updateFeed(randomUUID().toString());

        assertErrorResponse(response, WRONG_FEED_ID);
    }

    @Test
    public void whenFeedIdCanNotBeParsedThenErrorReturns() {
        final String response = updateFeed("12345678");

        assertErrorResponse(response, INVALID_FEED_ID);
    }

}
