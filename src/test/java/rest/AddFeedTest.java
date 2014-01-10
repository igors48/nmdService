package rest;

import nmd.rss.collector.error.ErrorCode;
import nmd.rss.collector.rest.responses.FeedHeaderResponse;
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
    public void whenFeedAddedThenItIsReturnedInList() {
        final FeedIdResponse feedIdResponse = addFirstFeed();

        final FeedHeadersResponse feedsResponse = getFeedHeaders();

        final FeedHeaderResponse feedHeaderResponse = new FeedHeaderResponse(FIRST_FEED_URL, feedIdResponse.getFeedId().toString(), FIRST_FEED_TITLE);

        assertEquals(1, feedsResponse.getHeaders().size());
        assertEquals(feedHeaderResponse, feedsResponse.getHeaders().get(0));
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

    @Test
    public void whenFeedUrlIsEmptyThenErrorReturns() {
        final String response = addFeed("");

        assertErrorResponse(response, ErrorCode.URL_FETCH_ERROR);
    }

}
