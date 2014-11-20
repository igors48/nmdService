package unit.feed.instagram;

import org.junit.Test;

import static nmd.orb.sources.instagram.InstagramClientTools.getInstagramUserName;
import static nmd.orb.sources.instagram.InstagramClientTools.isItInstagramUrl;
import static org.junit.Assert.*;

/**
 * @author : igu
 */
public class InstagramUrlToolsTest {

    private static final String VALID_INSTAGRAM_URL = "http://instagram.com/skif48";

    @Test
    public void instagramUrlDetection() {
        assertFalse(isItInstagramUrl(""));
        assertFalse(isItInstagramUrl(null));
        assertTrue(isItInstagramUrl(VALID_INSTAGRAM_URL));
    }

    @Test
    public void instagramUserNameExtraction() {
        assertEquals("skif48", getInstagramUserName(VALID_INSTAGRAM_URL));

        assertNull(getInstagramUserName("http://instagram.com/"));
        assertNull(getInstagramUserName("http://domain.com/"));
        assertNull(getInstagramUserName(""));
        assertNull(getInstagramUserName(null));
    }

}
