package feed.exporter;

import nmd.rss.collector.rest.ServletTools;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 18.05.13
 */
public class ExporterServletToolsParseFeedIdTest {

    @Test
    public void parse() {
        UUID origin = UUID.randomUUID();
        UUID parsed = ServletTools.parseFeedId("/" + origin);

        assertEquals(origin, parsed);
    }

    @Test
    public void ifFeedIdNotFoundNullReturns() {
        UUID parsed = ServletTools.parseFeedId("/");

        assertNull(parsed);
    }

    @Test
    public void ifFeedIdCanNotParseNullReturns() {
        UUID parsed = ServletTools.parseFeedId("/qwdwqde");

        assertNull(parsed);
    }

    @Test
    public void ifPathInfoIsEmptyNullReturns() {
        UUID parsed = ServletTools.parseFeedId("");

        assertNull(parsed);
    }

    @Test
    public void ifPathInfoIsNullNullReturns() {
        UUID parsed = ServletTools.parseFeedId(null);

        assertNull(parsed);
    }

}
