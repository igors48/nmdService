package rest;

import nmd.rss.collector.rest.responses.FeedIdResponse;
import nmd.rss.collector.rest.responses.FeedMergeReportResponse;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static nmd.rss.collector.error.ErrorCode.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

        final FeedMergeReportResponse report = updateCurrentFeedWithReport();

        final FeedMergeReportResponse expected = new FeedMergeReportResponse(FIRST_FEED_URL, feedIdResponse.getFeedId(), 0, 100, 0);

        assertEquals(expected, report);
    }

    @Test
    public void whenThereAreMoreThanOneFeedsThenTheyUpdateSequentally() {
        final FeedIdResponse firstFeedIdResponse = addFirstFeed();
        final FeedIdResponse secondFeedIdResponse = addSecondFeed();

        final FeedMergeReportResponse firstReport = updateCurrentFeedWithReport();
        final FeedMergeReportResponse secondReport = updateCurrentFeedWithReport();

        final Set<UUID> updatedFeedsId = new HashSet<UUID>() {{
            add(firstReport.getFeedId());
            add(secondReport.getFeedId());
        }};

        assertTrue(updatedFeedsId.contains(firstFeedIdResponse.getFeedId()));
        assertTrue(updatedFeedsId.contains(secondFeedIdResponse.getFeedId()));
    }

    @Test
    public void whenFeedIdExistsThenFeedUpdatesAndReportReturns() {
        final FeedIdResponse feedIdResponse = addFirstFeed();

        final FeedMergeReportResponse report = updateCurrentFeedWithReport();

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
