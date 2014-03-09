package unit.feed.twitter;

import nmd.rss.collector.feed.FeedItem;
import nmd.rss.collector.twitter.entities.Urls;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static nmd.rss.collector.twitter.TweetConversionTools.convertToItem;
import static nmd.rss.collector.twitter.TweetConversionTools.parse;
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
    public void whenTweetEntitiesIsNullThenNullReturns() {
        this.tweet.setEntities(null);

        assertNull(convertToItem(this.tweet, SOME_DATE));
    }

    @Test
    public void whenUrlsInTweetEntitiesIsNullThenNullReturns() {
        this.tweet.getEntities().setUrls(null);

        assertNull(convertToItem(this.tweet, SOME_DATE));
    }

    @Test
    public void whenUrlsInTweetEntitiesIsEmptyThenNullReturns() {
        this.tweet.getEntities().setUrls(new ArrayList<Urls>());

        assertNull(convertToItem(this.tweet, SOME_DATE));
    }

    @Test
    public void whenFirstElementInUrlsInTweetEntitiesIsNullThenNullReturns() {
        this.tweet.getEntities().getUrls().set(0, null);

        assertNull(convertToItem(this.tweet, SOME_DATE));
    }

    @Test
    public void whenExpandedUrlOfFirstElementInUrlsInTweetEntitiesIsNullThenNullReturns() {
        this.tweet.getEntities().getUrls().get(0).setExpanded_url(null);

        assertNull(convertToItem(this.tweet, SOME_DATE));
    }

    @Test
    public void whenExpandedUrlOfFirstElementInUrlsInTweetEntitiesIsEmptyThenNullReturns() {
        this.tweet.getEntities().getUrls().get(0).setExpanded_url("");

        assertNull(convertToItem(this.tweet, SOME_DATE));
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
