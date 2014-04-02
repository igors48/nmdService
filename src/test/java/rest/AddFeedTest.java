package rest;

import nmd.rss.collector.error.ErrorCode;
import nmd.rss.collector.rest.responses.FeedHeadersResponse;
import nmd.rss.collector.rest.responses.FeedIdResponse;
import nmd.rss.collector.rest.responses.payload.FeedHeaderPayload;
import org.junit.Test;

import static nmd.rss.reader.Category.MAIN_CATEGORY_ID;
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

        final FeedHeaderPayload feedHeaderPayload = new FeedHeaderPayload(FIRST_FEED_URL, feedIdResponse.getFeedId().toString(), FIRST_FEED_TITLE);

        assertEquals(1, feedsResponse.getHeaders().size());
        assertEquals(feedHeaderPayload, feedsResponse.getHeaders().get(0));
    }

    @Test
    public void whenSameFeedAddedSecondTimeThenUuidFromFirstAttemptReturns() {
        final FeedIdResponse firstFeedIdResponse = addFirstFeed();
        final FeedIdResponse secondFeedIdResponse = addFirstFeed();

        assertEquals(firstFeedIdResponse, secondFeedIdResponse);
    }

    @Test
    public void whenFeedIsCorruptThenErrorReturns() {
        final String response = addFeed(INVALID_FEED_URL, MAIN_CATEGORY_ID);

        assertErrorResponse(response, ErrorCode.FEED_PARSE_ERROR);
    }

    @Test
    public void whenFeedIsUnreachableThenErrorReturns() {
        final String response = addFeed(UNREACHABLE_FEED_URL, MAIN_CATEGORY_ID);

        assertErrorResponse(response, ErrorCode.URL_FETCH_ERROR);
    }

    @Test
    public void whenFeedUrlIsEmptyThenErrorReturns() {
        final String response = addFeed("", MAIN_CATEGORY_ID);

        assertErrorResponse(response, ErrorCode.INVALID_FEED_URL);
    }

}
