package rest.reads;

import nmd.rss.collector.rest.responses.FeedIdResponse;
import org.junit.Test;
import rest.AbstractRestTest;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 26.01.14
 */
public class MarkAllFeedItemsAsReadTest extends AbstractRestTest {

    @Test
    public void whenItemMarkedAsReadInExistsFeedThenSuccessResponseReturns() {
        final FeedIdResponse feedIdResponse = addFirstFeed();

        assertSuccessResponse(markAllItemsAsRead(feedIdResponse.feedId.toString()));
    }

}
