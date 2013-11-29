package rest;

import org.junit.After;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.path.json.JsonPath.from;

/**
 * User: igu
 * Date: 28.11.13
 */
public abstract class AbstractRestTest {

    protected static final String CLEAR_SERVLET_URL = "/secure/v01/clear";
    protected static final String FEEDS_SERVLET_URL = "/secure/v01/feeds";

    protected static final String FIRST_FEED_URL = "http://localhost:8080/feed/feed_win_1251.xml";
    protected static final String INVALID_FEED_URL = "http://localhost:8080/feed/not_exist.xml";

    @After
    public void after() {
        clearState();
    }

    protected static void clearState() {
        given().body("").post(CLEAR_SERVLET_URL);
    }

    protected static String addFirstFeed() {
        final String response = addFeed(FIRST_FEED_URL);

        return from(response).getString("feedId");
    }

    protected static String addFeed(final String url) {
        return given().body(url).post(FEEDS_SERVLET_URL).asString();
    }

}
