package unit.tool;

import nmd.orb.util.Parameter;
import nmd.orb.util.UrlTools;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

    @Test
    public void urlValidationSpecialCases() {
        assertTrue(Parameter.isValidUrl("http://rgg.zone"));
        assertTrue(Parameter.isValidUrl("http://dnr.today"));
    }

    @Test
    public void getBaseLinkTest() {
        assertEquals("http://domain.com", UrlTools.getBaseLink("http://domain.com"));
        assertEquals("http://domain.com", UrlTools.getBaseLink("http://domain.com//ad"));
        assertEquals("http://domain.com:80", UrlTools.getBaseLink("http://domain.com:80//ad"));
    }

}
