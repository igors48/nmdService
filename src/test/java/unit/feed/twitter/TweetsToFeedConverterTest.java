package unit.feed.twitter;

import nmd.orb.error.ErrorCode;
import nmd.orb.error.ServiceException;
import nmd.orb.feed.Feed;
import nmd.orb.sources.twitter.entities.Tweet;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static nmd.orb.sources.twitter.TweetConversionTools.convertToFeed;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 02.03.14
 */
public class TweetsToFeedConverterTest extends AbstractTweetConverterTestBase {

    private static final String TWITTER_URL = "http://domain.com/twitter_url";
    private static Date SOME_DATE = new Date(1);

    @Test
    public void whenTweetsListIsEmptyThenNullReturns() throws ServiceException {

        try {
            convertToFeed(TWITTER_URL, new ArrayList<Tweet>(), SOME_DATE);

            fail();
        } catch (ServiceException exception) {
            assertEquals(ErrorCode.TWITTER_NO_TWEETS, exception.getError().code);
        }
    }

    @Test
    public void whenTweetsListIsNullThenNullReturns() throws ServiceException {

        try {
            convertToFeed(TWITTER_URL, null, SOME_DATE);

            fail();
        } catch (ServiceException exception) {
            assertEquals(ErrorCode.TWITTER_NO_TWEETS, exception.getError().code);
        }
    }

    @Test
    public void whenTweetsListContainsTweetsThenFeedReturns() throws ServiceException {
        final Feed feed = convertToFeed(TWITTER_URL, this.tweets, SOME_DATE);

        assertEquals(1, feed.items.size());
    }

}
