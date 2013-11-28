package unit.tool;

import nmd.rss.collector.util.UrlTools;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 31.08.13
 */
public class UrlToolsTest {

    @Test
    public void lastSlashesTruncatedWhileNormalize() {
        assertEquals("dfg", UrlTools.normalizeUrl("dfg/"));
        assertEquals("dfg", UrlTools.normalizeUrl("dfg//"));
        assertEquals("dfg", UrlTools.normalizeUrl("dfg\\"));
        assertEquals("dfg", UrlTools.normalizeUrl("dfg\\\\"));
    }

    @Test
    public void normalUrlDidNotChange() {
        assertEquals("dfg", UrlTools.normalizeUrl("dfg"));
    }

    @Test
    public void urlConvertedToLowerCaseWhileNormalize() {
        assertEquals("dfg", UrlTools.normalizeUrl("dFg"));
    }
}
