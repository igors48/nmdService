package http.cron;

import http.AbstractHttpTest;
import org.junit.Test;

/**
 * Created by igor on 17.01.2015.
 */
public class CronTest extends AbstractHttpTest {

    @Test
    public void smoke() {
        assertSuccessResponse(executeCronJob());
    }

}
