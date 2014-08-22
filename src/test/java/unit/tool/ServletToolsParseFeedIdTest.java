package unit.tool;

import nmd.rss.http.tools.ServletTools;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 18.05.13
 */
public class ServletToolsParseFeedIdTest {

    @Test
    public void ifFeedIdNotFoundNullReturns() {
        final UUID parsed = ServletTools.parseFeedId("/");

        assertNull(parsed);
    }

    @Test
    public void ifFeedIdCanNotParseNullReturns() {
        final UUID parsed = ServletTools.parseFeedId("/qwdwqde");

        assertNull(parsed);
    }

    @Test
    public void ifPathInfoIsEmptyNullReturns() {
        final UUID parsed = ServletTools.parseFeedId("");

        assertNull(parsed);
    }

    @Test
    public void ifPathInfoIsNullNullReturns() {
        final UUID parsed = ServletTools.parseFeedId(null);

        assertNull(parsed);
    }

}
