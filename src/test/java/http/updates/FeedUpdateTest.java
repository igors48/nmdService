package http.updates;

import http.AbstractHttpTest;
import nmd.orb.http.responses.FeedIdResponse;
import nmd.orb.http.responses.FeedMergeReportResponse;
import nmd.orb.http.responses.FeedSeriesUpdateResponse;
import org.junit.Test;

import static java.util.UUID.randomUUID;
import static nmd.orb.error.ErrorCode.INVALID_FEED_ID;
import static nmd.orb.error.ErrorCode.WRONG_FEED_ID;
import static org.junit.Assert.assertEquals;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 01.12.13
 */
public class FeedUpdateTest extends AbstractHttpTest {

    @Test
    public void whenCurrentFeedExistsThenItUpdatesAndReportReturns() {
        addFirstFeed();

        final FeedSeriesUpdateResponse report = updateCurrentFeedWithReport();

        assertEquals(0, report.errors.size());
        assertEquals(1, report.updates.size());
    }

    @Test
    public void whenFeedIdExistsThenFeedUpdatesAndReportReturns() {
        final FeedIdResponse feedIdResponse = addFirstFeed();

        final FeedMergeReportResponse report = updateFeedWithReport(feedIdResponse.feedId.toString());

        final FeedMergeReportResponse expected = FeedMergeReportResponse.create(feedIdResponse.feedId, FIRST_FEED_URL, 0, 100, 0);

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
