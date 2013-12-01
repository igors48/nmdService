package rest;

import org.junit.Test;

import static nmd.rss.collector.error.ErrorCode.NO_SCHEDULED_TASK;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 01.12.13
 */
public class FeedUpdateTest extends AbstractRestTest {

    @Test
    public void whenNoCurrentFeedThenErrorReturns() {
        final String response = updateCurrentFeed();

        assertErrorResponse(response, NO_SCHEDULED_TASK);
    }

}
