package http.reads;

import http.AbstractHttpTest;
import nmd.orb.error.ErrorCode;
import nmd.orb.http.responses.FeedIdResponse;
import nmd.orb.http.responses.FeedItemsReportResponse;
import nmd.orb.http.responses.FeedReadReportsResponse;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 03.12.13
 */
public class ReadFeedTest extends AbstractHttpTest {

    @Test
    public void whenFeedExistsThenReadReportReturns() {
        addFirstFeed();

        final FeedReadReportsResponse response = getReadsReport();

        assertFalse(response.reports.isEmpty());
    }

    @Test
    public void whenThereAreNoFeedsThenNoReportsReturn() {
        final FeedReadReportsResponse response = getReadsReport();

        assertTrue(response.reports.isEmpty());
    }

    @Test
    public void whenFeedIdIsInvalidThenErrorReturns() {
        assertErrorResponse(getFeedItemsReportAsString("111"), ErrorCode.INVALID_FEED_ID);
    }

    @Test
    public void whenFeedIdIsNotFoundThenErrorReturns() {
        assertErrorResponse(getFeedItemsReportAsString(UUID.randomUUID().toString()), ErrorCode.WRONG_FEED_ID);
    }

    @Test
    public void whenFeedIdIsFoundThenReportReturns() {
        final FeedIdResponse feedIdResponse = addFirstFeed();

        final FeedItemsReportResponse feedItemsReportResponse = getFeedItemsReport(feedIdResponse.feedId.toString());

        assertFalse(feedItemsReportResponse.title.isEmpty());
        assertFalse(feedItemsReportResponse.reports.isEmpty());

        assertEquals(0, feedItemsReportResponse.read);
        assertEquals(0, feedItemsReportResponse.readLater);
        assertTrue(feedItemsReportResponse.addedSinceLastView > 0);
        assertTrue(feedItemsReportResponse.notRead > 0);
    }

    @Test
    public void whenNotReadFilterSetThenReportReturns() {
        final FeedIdResponse feedIdResponse = addFirstFeed();

        final FeedItemsReportResponse feedItemsReportResponse = getNotReadFeedItemsFilteredReport(feedIdResponse.feedId.toString());

        assertFalse(feedItemsReportResponse.title.isEmpty());
        assertFalse(feedItemsReportResponse.reports.isEmpty());

        assertEquals(0, feedItemsReportResponse.read);
        assertEquals(0, feedItemsReportResponse.readLater);
        assertTrue(feedItemsReportResponse.addedSinceLastView > 0);
        assertTrue(feedItemsReportResponse.notRead > 0);
    }

    @Test
    public void whenReadLaterFilterSetThenReportReturns() {
        final FeedIdResponse feedIdResponse = addFirstFeed();

        final FeedItemsReportResponse feedItemsReportResponse = getReadLaterFeedItemsFilteredReport(feedIdResponse.feedId.toString());

        assertFalse(feedItemsReportResponse.title.isEmpty());
        assertTrue(feedItemsReportResponse.reports.isEmpty());

        assertEquals(0, feedItemsReportResponse.read);
        assertEquals(0, feedItemsReportResponse.readLater);
        assertTrue(feedItemsReportResponse.addedSinceLastView > 0);
        assertTrue(feedItemsReportResponse.notRead > 0);
    }

    @Test
    public void whenAddedFilterSetThenReportReturns() {
        final FeedIdResponse feedIdResponse = addFirstFeed();

        final FeedItemsReportResponse feedItemsReportResponse = getAddedFeedItemsFilteredReport(feedIdResponse.feedId.toString());

        assertFalse(feedItemsReportResponse.title.isEmpty());
        assertFalse(feedItemsReportResponse.reports.isEmpty());

        assertEquals(0, feedItemsReportResponse.read);
        assertEquals(0, feedItemsReportResponse.readLater);
        assertTrue(feedItemsReportResponse.addedSinceLastView > 0);
        assertTrue(feedItemsReportResponse.notRead > 0);
    }

}
