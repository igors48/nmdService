package rest;

import nmd.rss.collector.rest.responses.FeedIdResponse;
import org.junit.Test;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 26.01.14
 */
public class MarkFeedItemAsReadLaterTest extends AbstractRestTest {

    @Test
    public void whenItemMarkedAsReadLaterInExistsFeedThenSuccessResponseReturns() {
        final FeedIdResponse feedIdResponse = addFirstFeed();

        assertSuccessResponse(markItemAsReadLater(feedIdResponse.feedId.toString(), "guid"));
    }

}
