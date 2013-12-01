package rest;

import nmd.rss.collector.error.ErrorCode;
import nmd.rss.collector.feed.Feed;
import nmd.rss.collector.feed.FeedParserException;
import org.junit.Test;

import java.util.UUID;

import static nmd.rss.collector.feed.FeedParser.parse;
import static org.junit.Assert.assertFalse;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 30.11.13
 */
public class FeedExportTest extends AbstractRestTest {

    @Test
    public void whenFeedIdExistsThenFeedDataReturnsInXmlFormat() throws FeedParserException {
        final String feedId = addFirstFeed();

        final String response = exportFeed(feedId);

        final Feed feed = parse(FIRST_FEED_URL, response);

        assertFalse(feed.items.isEmpty());
    }

    @Test
    public void whenFeedIdDoesNotExistThenErrorReturns() {
        final String response = exportFeed(UUID.randomUUID().toString());

        assertErrorResponse(response, ErrorCode.WRONG_FEED_ID.toString());
    }

    @Test
    public void whenFeedIdCanNotBeParsedThenErrorReturns() {
        final String response = exportFeed("12345678");

        assertErrorResponse(response, ErrorCode.INVALID_FEED_ID.toString());
    }

    @Test
    public void whenFeedIdIsEmptyThenErrorReturns() {
        final String response = exportFeed("");

        assertErrorResponse(response, ErrorCode.INVALID_FEED_ID.toString());
    }

}
