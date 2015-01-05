package http.reset;

import http.AbstractHttpTest;
import org.junit.Test;

/**
 * @author : igu
 */
public class ResetTest extends AbstractHttpTest {

    @Test
    public void whenResetThenSuccessResponseReturns() {
        resetServer();
    }

}
