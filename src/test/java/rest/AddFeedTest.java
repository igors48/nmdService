package rest;

import nmd.rss.collector.error.ErrorCode;
import nmd.rss.collector.rest.responses.FeedHeadersResponse;
import nmd.rss.collector.rest.responses.FeedIdResponse;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * User: igu
 * Date: 29.11.13
 */
public class AddFeedTest extends AbstractRestTest {

    @Test
    public void whenFeedAddedThenItsIdReturnsAsValidUuid() {
        addFirstFeed();
    }

    @Test
    public void whenSameFeedAddedSecondTimeThenUuidFromFirstAttemptReturns() {
        final FeedIdResponse firstFeedIdResponse = addFirstFeed();
        final FeedIdResponse secondFeedIdResponse = addFirstFeed();

        assertEquals(firstFeedIdResponse, secondFeedIdResponse);
    }

    @Test
    public void whenFeedIsCorruptThenErrorReturns() {
        final String response = addFeed(INVALID_FEED_URL);

        assertErrorResponse(response, ErrorCode.FEED_PARSE_ERROR);
    }

    @Test
    public void whenFeedIsUnreachableThenErrorReturns() {
        final String response = addFeed(UNREACHABLE_FEED_URL);

        assertErrorResponse(response, ErrorCode.URL_FETCH_ERROR);
    }

    //TODO finish it
    @Test
    public void whenFeedAddedThenItIsReturnedInList() {
        final FeedIdResponse feedIdResponse = addFirstFeed();

        final FeedHeadersResponse feedsResponse = getFeedHeaders();

        System.out.println(feedsResponse);
    }

}
