package rest.feeds;

import nmd.rss.collector.error.ErrorCode;
import org.junit.Test;
import rest.AbstractRestTest;

import static nmd.rss.reader.Category.MAIN_CATEGORY_ID;

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
