package http.reads;

import http.AbstractHttpTest;
import nmd.orb.http.responses.FeedIdResponse;
import org.junit.Test;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 26.01.14
 */
public class MarkFeedItemAsNotReadTest extends AbstractHttpTest {

    @Test
    public void whenItemMarkedAsNotReadInExistsFeedThenSuccessResponseReturns() {
        final FeedIdResponse feedIdResponse = addFirstFeed();

        assertSuccessResponse(markItemAsNotRead(feedIdResponse.feedId.toString(), "guid"));
    }

}
