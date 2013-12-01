package rest;

import nmd.rss.collector.rest.responses.FeedIdResponse;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
    public void whenSameFeedAddedSecondTimeThenUuidFromFirstAttemptReturns() {
        final FeedIdResponse firstFeedIdResponse = addFirstFeed();
        final FeedIdResponse secondFeedIdResponse = addFirstFeed();

        assertEquals(firstFeedIdResponse, secondFeedIdResponse);
    }

    //TODO finish it
    @Test
    public void whenFeedIsUnreachableThenErrorReturns() {
        final FeedIdResponse response = addFeed(INVALID_FEED_URL);

        System.out.println(response);
    }

}
