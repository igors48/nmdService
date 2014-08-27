package http.feeds;

import http.AbstractHttpTest;
import nmd.orb.error.ErrorCode;
import nmd.orb.feed.FeedHeader;
import nmd.orb.http.responses.FeedHeadersResponse;
import nmd.orb.http.responses.FeedIdResponse;
import org.junit.Test;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

/**
 * User: igu
 * Date: 09.01.14
 */
public class GetFeedHeadersTest extends AbstractHttpTest {

    @Test
    public void whenFeedIdDoesNotSpecifiedThenAllHeadersReturn() {
        addFirstFeed();
        addSecondFeed();

        final FeedHeadersResponse feedHeaders = getFeedHeaders();

        assertEquals(2, feedHeaders.headers.size());
    }

    @Test
    public void whenFeedIdIsExistsThenHeaderReturns() {
        final FeedIdResponse feedId = addFirstFeed();
        final String feedIdAsString = feedId.feedId.toString();

        final FeedHeadersResponse feedHeader = getFeedHeader(feedIdAsString);

        final FeedHeader header = new FeedHeader(feedId.feedId, FIRST_FEED_URL, FIRST_FEED_TITLE, FIRST_FEED_TITLE, FIRST_FEED_URL);
        final FeedHeadersResponse expected = FeedHeadersResponse.convert(Arrays.asList(header));

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
