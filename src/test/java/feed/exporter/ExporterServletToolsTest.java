package feed.exporter;

import nmd.rss.collector.exporter.ExporterServletTools;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 18.05.13
 */
public class ExporterServletToolsTest {

    @Test
    public void parse() {
        UUID origin = UUID.randomUUID();
        UUID parsed = ExporterServletTools.parseFeedId("/" + origin);

        assertEquals(origin, parsed);
    }

    @Test
    public void ifFeedIdNotFoundNullReturns() {
        UUID parsed = ExporterServletTools.parseFeedId("/");

        assertNull(parsed);
    }

    @Test
    public void ifFeedIdCanNotParseNullReturns() {
        UUID parsed = ExporterServletTools.parseFeedId("/qwdwqde");

        assertNull(parsed);
    }

    @Test
    public void ifPathInfoIsEmptyNullReturns() {
        UUID parsed = ExporterServletTools.parseFeedId("");

        assertNull(parsed);
    }

    @Test
    public void ifPathInfoIsNullNullReturns() {
        UUID parsed = ExporterServletTools.parseFeedId(null);

        assertNull(parsed);
    }

}
