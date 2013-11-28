package rest;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.UUID;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.path.json.JsonPath.from;
import static org.junit.Assert.assertEquals;

/**
 * User: igu
 * Date: 28.11.13
 */
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
public class RestTest {

    private UUID firstFeedId;

    @Test
    public void aa_whenFeedAddedThenItsIdReturnsAsCorrectUuid() {
        final String response = given().body("http://localhost:8080/feed/feed_win_1251.xml").post("/secure/v01/feeds").asString();
        final String feedId = from(response).getString("feedId");

        this.firstFeedId = UUID.fromString(feedId);
    }

    @Test
    public void ab_whenSameFeedAddedSecondTimeThenUuidFromFirstAttemptReturns() {
        final String response = given().body("http://localhost:8080/feed/feed_win_1251.xml").post("/secure/v01/feeds").asString();
        final String feedIdAsString = from(response).getString("feedId");

        final UUID feedId = UUID.fromString(feedIdAsString);

        assertEquals(this.firstFeedId, feedId);
    }

}
