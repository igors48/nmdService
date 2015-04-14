package http.exports;

import com.sun.syndication.io.FeedException;
import http.AbstractHttpTest;
import nmd.orb.error.ServiceException;
import nmd.orb.feed.Feed;
import nmd.orb.http.responses.FeedIdResponse;
import org.junit.Test;

import static java.util.UUID.randomUUID;
import static nmd.orb.error.ErrorCode.INVALID_FEED_ID;
import static nmd.orb.error.ErrorCode.WRONG_FEED_ID;
import static nmd.orb.sources.rss.FeedParser.parse;
import static org.junit.Assert.assertFalse;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 30.11.13
 */
public class FeedExportTest extends AbstractHttpTest {

    @Test
    public void whenFeedIdExistsThenFeedDataReturnsInXmlFormat() throws FeedException, ServiceException {
        final FeedIdResponse feedIdResponse = addFirstFeed();

        final String response = exportFeed(feedIdResponse.feedId.toString());

        final Feed feed = parse(FIRST_FEED_URL, response);

        assertFalse(feed.items.isEmpty());
    }

    @Test
    public void whenFeedIdDoesNotExistThenErrorReturns() {
        final String response = exportFeed(randomUUID().toString());

        assertErrorResponse(response, WRONG_FEED_ID);
    }

    @Test
    public void whenFeedIdCanNotBeParsedThenErrorReturns() {
        final String response = exportFeed("12345678");

        assertErrorResponse(response, INVALID_FEED_ID);
    }

    @Test
    public void whenFeedIdIsEmptyThenErrorReturns() {
        final String response = exportFeed("");

        assertErrorResponse(response, INVALID_FEED_ID);
    }

}
