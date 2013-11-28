package rest;

import com.jayway.restassured.RestAssured;
import org.junit.Test;

/**
 * User: igu
 * Date: 28.11.13
 */
public class RestTest {

    @Test
    public void smoke() {
        RestAssured.get("/");
    }

}
