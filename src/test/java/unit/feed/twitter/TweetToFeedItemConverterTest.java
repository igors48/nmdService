package unit.feed.twitter;

import nmd.orb.collector.feed.FeedItem;
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
        assertEquals(FIRST_TWEET_ENTITY_EXPANDED_URL, feedItem.link);
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
    public void whenTweetEntitiesIsNullThenTweetUrlIsUsed() {
        this.tweet.setEntities(null);

        assertEquals(makeTweetUrl(), convertToItem(this.tweet, SOME_DATE).link);
    }

    @Test
    public void whenUrlsInTweetEntitiesIsNullThenThenTweetUrlIsUsed() {
        this.tweet.getEntities().setUrls(null);

        assertEquals(makeTweetUrl(), convertToItem(this.tweet, SOME_DATE).link);
    }

    @Test
    public void whenUrlsInTweetEntitiesIsEmptyThenTweetUrlIsUsed() {
        this.tweet.getEntities().setUrls(new ArrayList<Urls>());

        assertEquals(makeTweetUrl(), convertToItem(this.tweet, SOME_DATE).link);
    }

    @Test
    public void whenFirstElementInUrlsInTweetEntitiesIsNullThenTweetUrlIsUsed() {
        this.tweet.getEntities().getUrls().set(0, null);

        assertEquals(makeTweetUrl(), convertToItem(this.tweet, SOME_DATE).link);
    }

    @Test
    public void whenExpandedUrlOfFirstElementInUrlsInTweetEntitiesIsNullThenTweetUrlIsUsed() {
        this.tweet.getEntities().getUrls().get(0).setExpanded_url(null);

        assertEquals(makeTweetUrl(), convertToItem(this.tweet, SOME_DATE).link);
    }

    @Test
    public void whenExpandedUrlOfFirstElementInUrlsInTweetEntitiesIsEmptyThenTweetUrlIsUsed() {
        this.tweet.getEntities().getUrls().get(0).setExpanded_url("");

        assertEquals(makeTweetUrl(), convertToItem(this.tweet, SOME_DATE).link);
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
