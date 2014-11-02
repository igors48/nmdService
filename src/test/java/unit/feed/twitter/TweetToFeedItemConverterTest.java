package unit.feed.twitter;

import nmd.orb.feed.FeedItem;
import nmd.orb.sources.twitter.entities.Urls;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static nmd.orb.sources.twitter.TweetConversionTools.convertToItem;
import static nmd.orb.sources.twitter.TweetConversionTools.parse;
import static org.junit.Assert.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 02.03.14
 */
public class TweetToFeedItemConverterTest extends AbstractTweetConverterTestBase {

    private static Date SOME_DATE = new Date(1);

    @Test
    public void smoke() {
        final FeedItem feedItem = convertToItem(this.tweet, SOME_DATE);

        assertFalse(feedItem.guid.isEmpty());
        assertEquals(FIRST_TWEET_ENTITY_EXPANDED_URL, feedItem.gotoLink);
        assertEquals(makeTweetUrl(), feedItem.link);
        assertEquals(TEXT, feedItem.title);
        assertEquals(TEXT, feedItem.description);
        assertEquals(parse(CREATED), feedItem.date);
        assertTrue(feedItem.dateReal);
    }

    @Test
    public void whenTweetTextIsNullThenNullReturns() {
        this.tweet.setText(null);

        assertNull(convertToItem(this.tweet, SOME_DATE));
    }

    @Test
    public void whenTweetTextIsEmptyThenNullReturns() {
        this.tweet.setText("");

        assertNull(convertToItem(this.tweet, SOME_DATE));
    }

    @Test
    public void whenTweetEntitiesIsNullThenTweetUrlIsUsedAsGotoLink() {
        this.tweet.setEntities(null);

        final FeedItem feedItem = convertToItem(this.tweet, SOME_DATE);

        assertEquals(makeTweetUrl(), feedItem.link);
        assertEquals(makeTweetUrl(), feedItem.gotoLink);
    }

    @Test
    public void whenUrlsInTweetEntitiesIsNullThenThenTweetUrlIsUsedAsGotoLink() {
        this.tweet.getEntities().setUrls(null);

        final FeedItem feedItem = convertToItem(this.tweet, SOME_DATE);

        assertEquals(makeTweetUrl(), feedItem.link);
        assertEquals(makeTweetUrl(), feedItem.gotoLink);
    }

    @Test
    public void whenUrlsInTweetEntitiesIsEmptyThenTweetUrlIsUsedAsGotoLink() {
        this.tweet.getEntities().setUrls(new ArrayList<Urls>());

        final FeedItem feedItem = convertToItem(this.tweet, SOME_DATE);

        assertEquals(makeTweetUrl(), feedItem.link);
        assertEquals(makeTweetUrl(), feedItem.gotoLink);
    }

    @Test
    public void whenFirstElementInUrlsInTweetEntitiesIsNullThenTweetUrlIsUsedAsGotoLink() {
        this.tweet.getEntities().getUrls().set(0, null);

        final FeedItem feedItem = convertToItem(this.tweet, SOME_DATE);

        assertEquals(makeTweetUrl(), feedItem.link);
        assertEquals(makeTweetUrl(), feedItem.gotoLink);
    }

    @Test
    public void whenExpandedUrlOfFirstElementInUrlsInTweetEntitiesIsNullThenTweetUrlIsUsedAsGotoLink() {
        this.tweet.getEntities().getUrls().get(0).setExpanded_url(null);

        final FeedItem feedItem = convertToItem(this.tweet, SOME_DATE);

        assertEquals(makeTweetUrl(), feedItem.link);
        assertEquals(makeTweetUrl(), feedItem.gotoLink);
    }

    @Test
    public void whenExpandedUrlOfFirstElementInUrlsInTweetEntitiesIsEmptyThenTweetUrlIsUsedAsGotoLink() {
        this.tweet.getEntities().getUrls().get(0).setExpanded_url("");

        final FeedItem feedItem = convertToItem(this.tweet, SOME_DATE);

        assertEquals(makeTweetUrl(), feedItem.link);
        assertEquals(makeTweetUrl(), feedItem.gotoLink);
    }

    @Test
    public void whenTweetDateIsNullThenAlternativeDateUsed() {
        this.tweet.setCreated_at(null);

        final FeedItem feedItem = convertToItem(this.tweet, SOME_DATE);

        assertEquals(SOME_DATE, feedItem.date);
        assertFalse(feedItem.dateReal);
    }

    @Test
    public void whenTweetDateIsNotParseableThenAlternativeDateUsed() {
        this.tweet.setCreated_at("Russia wants war");

        final FeedItem feedItem = convertToItem(this.tweet, SOME_DATE);

        assertEquals(SOME_DATE, feedItem.date);
        assertFalse(feedItem.dateReal);
    }

}
