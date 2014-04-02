package rest;

import nmd.rss.collector.error.ErrorCode;
import nmd.rss.collector.rest.responses.FeedHeadersResponse;
import nmd.rss.collector.rest.responses.FeedIdResponse;
import nmd.rss.collector.rest.responses.helper.FeedHeaderHelper;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

/**
 * User: igu
 * Date: 09.01.14
 */
public class GetFeedHeadersTest extends AbstractRestTest {

    @Test
    public void whenFeedIdDoesNotSpecifiedThenAllHeadersReturn() {
        addFirstFeed();
        addSecondFeed();

        final FeedHeadersResponse feedHeaders = getFeedHeaders();

        assertEquals(2, feedHeaders.getHeaders().size());
    }

    @Test
    public void whenFeedIdIsExistsThenHeaderReturns() {
        final FeedIdResponse feedId = addFirstFeed();
        final String feedIdAsString = feedId.getFeedId().toString();

        final FeedHeaderHelper feedHeader = getFeedHeader(feedIdAsString);
        final FeedHeaderHelper expected = new FeedHeaderHelper(FIRST_FEED_URL, feedIdAsString, FIRST_FEED_TITLE);

        assertEquals(expected, feedHeader);
    }

    @Test
    public void whenFeedIdIsWrongThenErrorReturns() {
        final String response = getFeedHeaderAsString(UUID.randomUUID().toString());

        assertErrorResponse(response, ErrorCode.WRONG_FEED_ID);
    }

    @Test
    public void whenFeedIdIsInvalidThenErrorReturns() {
        final String response = getFeedHeaderAsString("invalid");

        assertErrorResponse(response, ErrorCode.INVALID_FEED_ID);
    }

}
