package unit.tool;

import org.junit.Test;

import static nmd.orb.sources.Source.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author : igu
 */
public class SourceDetectionTest {

    @Test
    public void smoke() {
        assertNull(detect(""));
        assertNull(detect("*"));
        assertEquals(RSS, detect("http://domain.com"));
        assertEquals(INSTAGRAM, detect("http://instagram.com"));
        assertEquals(TWITTER, detect("http://twitter.com"));
    }

}
