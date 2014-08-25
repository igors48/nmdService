package unit.tool;

import org.junit.Test;

import static nmd.orb.util.HtmlTools.cleanupDescription;
import static org.junit.Assert.assertEquals;

/**
 * @author : igu
 */
public class HtmlCleanerTest {

    @Test
    public void smoke() {
        assertCleaned("a", "a");
        assertCleaned("a<p>b</p>", "a<p>b");
    }

    private static void assertCleaned(final String expected, final String source) {
        assertEquals(expected, stripNewLines(cleanupDescription(source)));
    }

    private static String stripNewLines(final String string) {
        return string.replaceAll("\\r\\n|\\r|\\n", "");
    }

}
