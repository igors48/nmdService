package rest;

import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.path.json.JsonPath.from;
import static org.junit.Assert.assertEquals;

/**
 * User: igu
 * Date: 28.11.13
 */
public class RestTest {

    @Before
    public void before() {
        clearState();
    }

    @Test
    public void whenFeedAddedThenItsIdReturnsAsValidUuid() {
        final String feedId = addFirstFeed();

        UUID.fromString(feedId);
    }

    @Test
    public void whenSameFeedAddedSecondTimeThenUuidFromFirstAttemptReturns() {
        String firstFeedIdAsString = addFirstFeed();
        String secondFeedIdAsString = addFirstFeed();

        assertEquals(firstFeedIdAsString, secondFeedIdAsString);
    }

    private static void clearState() {
        given().body("").post("/secure/v01/clear");
    }

    private static String addFirstFeed() {
        final String response = given().body("http://localhost:8080/feed/feed_win_1251.xml").post("/secure/v01/feeds").asString();

        return from(response).getString("feedId");
    }

}
