package rest;

import nmd.rss.collector.rest.responses.FeedIdResponse;
import nmd.rss.collector.rest.responses.FeedMergeReportResponse;
import org.junit.Test;

import static nmd.rss.collector.error.ErrorCode.NO_SCHEDULED_TASK;
import static org.junit.Assert.assertEquals;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 01.12.13
 */
public class FeedUpdateTest extends AbstractRestTest {

    @Test
    public void whenNoCurrentFeedThenErrorReturns() {
        final String response = updateCurrentFeed();

        assertErrorResponse(response, NO_SCHEDULED_TASK);
    }

    @Test
    public void whenCurrentFeedExistsThenItUpdatesAndReportReturns() {
        final FeedIdResponse feedIdResponse = addFirstFeed();

        final String response = updateCurrentFeed();
        final FeedMergeReportResponse report = GSON.fromJson(response, FeedMergeReportResponse.class);

        final FeedMergeReportResponse expected = new FeedMergeReportResponse(FIRST_FEED_URL, feedIdResponse.getFeedId(), 0, 100, 0);

        assertEquals(expected, report);
    }

    //more than one update sequentally
    //concrete feed updates
}
