package http.content;

import http.AbstractHttpTest;
import org.junit.Test;

/**
 * @author : igu
 */
public class ContentFilterTest extends AbstractHttpTest {

    @Test
    public void smoke() {
        assertSuccessResponse(filterContent());
    }

}
