package rest;

import com.google.gson.Gson;
import nmd.rss.collector.error.ErrorCode;
import nmd.rss.collector.rest.responses.*;
import org.junit.After;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

/**
 * User: igu
 * Date: 28.11.13
 */
public abstract class AbstractRestTest {

    //TODO assertResponseSuccess

    protected static final Gson GSON = new Gson();

    protected static final String CLEAR_SERVLET_URL = "/secure/v01/clear/";
    protected static final String FEEDS_SERVLET_URL = "/secure/v01/feeds/";
    protected static final String UPDATES_SERVLET_URL = "/secure/v01/updates/";
    protected static final String EXPORTS_SERVLET_URL = "/v01/feeds/";

    protected static final String FIRST_FEED_URL = "http://localhost:8080/feed/feed_win_1251.xml";
    protected static final String SECOND_FEED_URL = "http://localhost:8080/feed/feed_win_1251_2.xml";
    protected static final String INVALID_FEED_URL = "http://localhost:8080/feed/not_exist.xml";
    protected static final String UNREACHABLE_FEED_URL = "http://localhost:8081/feed/not_exist.xml";

    @After
    public void after() {
        clearState();
    }

    protected static void clearState() {
        given().body("").post(CLEAR_SERVLET_URL);
    }

    protected static FeedIdResponse addFirstFeed() {
        return GSON.fromJson(addFeed(FIRST_FEED_URL), FeedIdResponse.class);
    }

    protected static FeedIdResponse addSecondFeed() {
        return GSON.fromJson(addFeed(SECOND_FEED_URL), FeedIdResponse.class);
    }

    protected static String addFeed(final String url) {
        return given().body(url).post(FEEDS_SERVLET_URL).asString();
    }

    protected static FeedHeadersResponse getFeedHeaders() {
        final String response = given().get(FEEDS_SERVLET_URL).asString();

        return GSON.fromJson(response, FeedHeadersResponse.class);
    }

    protected static String deleteFeed(final String feedId) {
        return given().delete(FEEDS_SERVLET_URL + feedId).asString();
    }

    protected static String exportFeed(final String feedId) {
        return given().get(EXPORTS_SERVLET_URL + feedId).asString();
    }

    protected static String updateFeed(final String feedId) {
        return given().get(UPDATES_SERVLET_URL + feedId).asString();
    }

    protected static String updateCurrentFeed() {
        return updateFeed("");
    }

    protected static void assertErrorResponse(final String response, final ErrorCode errorCode) {
        final ErrorResponse errorResponse = GSON.fromJson(response, ErrorResponse.class);

        assertEquals(ResponseType.ERROR, errorResponse.getStatus());
        assertEquals(errorCode, errorResponse.getCode());
    }

    protected static void assertSuccessResponse(final String response) {
        final SuccessMessageResponse successResponse = GSON.fromJson(response, SuccessMessageResponse.class);

        assertEquals(ResponseType.SUCCESS, successResponse.getStatus());
    }

}
