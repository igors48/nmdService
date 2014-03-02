package unit.feed.twitter;

import nmd.rss.collector.feed.Feed;
import nmd.rss.collector.twitter.entities.Tweet;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static nmd.rss.collector.twitter.TweetConversionTools.convertToFeed;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 02.03.14
 */
public class TweetsToFeedConverterTest extends AbstractTweetConverterTestBase {

    private static final String TWITTER_URL = "twitter_url";
    private static Date SOME_DATE = new Date(1);

    @Test
    public void whenTweetsListIsEmptyThenNullReturns() {
        final Feed feed = convertToFeed(TWITTER_URL, new ArrayList<Tweet>(), SOME_DATE);

        assertNull(feed);
    }

    @Test
    public void whenTweetsListIsNullThenNullReturns() {
        final Feed feed = convertToFeed(TWITTER_URL, null, SOME_DATE);

        assertNull(feed);
    }

    @Test
    public void whenTweetsListContainsTweetsThenFeedReturns() {
        final Feed feed = convertToFeed(TWITTER_URL, this.tweets, SOME_DATE);

        assertEquals(1, feed.items.size());
    }

}
