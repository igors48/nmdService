package rest;

import nmd.rss.collector.rest.responses.FeedHeadersResponse;
import org.junit.Test;

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
    /*
    @Test
    public void whenFeedIdIsExistsThenHeaderReturns() {
        final FeedIdResponse feedId = addFirstFeed();
    }
    */
}
