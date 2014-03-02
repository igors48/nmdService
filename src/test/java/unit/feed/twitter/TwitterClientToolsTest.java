package unit.feed.twitter;

import org.junit.Test;

import static nmd.rss.collector.twitter.TwitterClientTools.isItTwitterUrl;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 02.03.14
 */
public class TwitterClientToolsTest {

    @Test
    public void twitterUrlDetectionTest() {
        assertFalse(isItTwitterUrl(""));
        assertFalse(isItTwitterUrl(null));
        assertTrue(isItTwitterUrl("https://twitter.com/adme_ru"));

    }

}
