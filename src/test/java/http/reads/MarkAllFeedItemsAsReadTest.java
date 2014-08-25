package http.reads;

import http.AbstractHttpTest;
import nmd.rss.http.responses.FeedIdResponse;
import org.junit.Test;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 26.01.14
 */
public class MarkAllFeedItemsAsReadTest extends AbstractHttpTest {

    @Test
    public void whenItemMarkedAsReadInExistsFeedThenSuccessResponseReturns() {
        final FeedIdResponse feedIdResponse = addFirstFeed();

        assertSuccessResponse(markAllItemsAsRead(feedIdResponse.feedId.toString()));
    }

}
