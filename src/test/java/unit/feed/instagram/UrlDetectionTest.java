package unit.feed.instagram;

import org.junit.Test;

import static nmd.orb.sources.instagram.InstagramClientTools.isItInstagramUrl;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author : igu
 */
public class UrlDetectionTest {

    @Test
    public void instagramUrlDetection() {
        assertFalse(isItInstagramUrl(""));
        assertFalse(isItInstagramUrl(null));
        assertTrue(isItInstagramUrl("http://instagram.com/skif48"));
    }

}
