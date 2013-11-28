package rest;

import com.jayway.restassured.RestAssured;
import org.junit.Test;

/**
 * User: igu
 * Date: 28.11.13
 */
public class RestTest {

    //http://localhost:8080/feed/feed_win_1251.xml

    @Test
    public void smoke() {
        RestAssured.get("/");
    }

}
