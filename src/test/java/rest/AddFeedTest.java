package rest;

import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

/**
 * User: igu
 * Date: 29.11.13
 */
public class AddFeedTest extends AbstractRestTest {

    @Test
    public void whenFeedAddedThenItsIdReturnsAsValidUuid() {
        final String feedId = addFirstFeed();

        UUID.fromString(feedId);
    }

    @Test
    public void whenSameFeedAddedSecondTimeThenUuidFromFirstAttemptReturns() {
        final String firstFeedIdAsString = addFirstFeed();
        final String secondFeedIdAsString = addFirstFeed();

        assertEquals(firstFeedIdAsString, secondFeedIdAsString);
    }

    @Test
    public void whenFeedIsUnreachableThenErrorReturns() {
        final String response = addFeed(INVALID_FEED_URL);

        System.out.println(response);
    }

}
