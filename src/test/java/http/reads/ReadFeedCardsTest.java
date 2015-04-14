package http.reads;

import http.AbstractHttpTest;
import nmd.orb.http.responses.FeedIdResponse;
import nmd.orb.http.responses.FeedItemsCardsReportResponse;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 03.12.13
 */
public class ReadFeedCardsTest extends AbstractHttpTest {

    @Test
    public void initialFeedCardsReportReturns() {
        final FeedIdResponse feedIdResponse = addFirstFeed();

        final FeedItemsCardsReportResponse response = getReadsCardsInitialReport(feedIdResponse.feedId.toString(), "4");

        assertEquals(5, response.reports.size());
    }

    @Test
    public void feedCardsReportReturns() {
        final FeedIdResponse feedIdResponse = addFirstFeed();
        final FeedItemsCardsReportResponse initialReport = getReadsCardsInitialReport(feedIdResponse.feedId.toString(), "4");

        final String secondItemId = initialReport.reports.get(1).itemId;

        final FeedItemsCardsReportResponse report = getReadsCardsReport(feedIdResponse.feedId.toString(), secondItemId, "next", "4");
        assertEquals(5, report.reports.size());
    }

}
