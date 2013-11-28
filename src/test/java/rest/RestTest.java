package rest;

import org.junit.Test;

import static com.jayway.restassured.RestAssured.given;

/**
 * User: igu
 * Date: 28.11.13
 */
public class RestTest {

    @Test
    public void smoke() {
        given().
                body("http://localhost:8080/feed/feed_win_1251.xml").
                expect().statusCode(200).
                when().post("/secure/v01/feeds");
    }

}
