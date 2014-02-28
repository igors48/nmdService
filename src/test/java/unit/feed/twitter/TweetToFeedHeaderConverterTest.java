package unit.feed.twitter;

import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.collector.twitter.entities.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static nmd.rss.collector.twitter.TweetConversionTools.convertToHeader;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 28.02.14
 */
public class TweetToFeedHeaderConverterTest {

    private static final String FIRST_USER_ENTITY_URL = "firstUserEntityUrl";
    private static final String FIRST_USER_ENTITY_EXPANDED_URL = "firstUserEntityExpandedUrl";
    private static final String SECOND_USER_ENTITY_URL = "secondUserEntityUrl";
    private static final String SECOND_USER_ENTITY_EXPANDED_URL = "secondUserEntityExpandedUrl";
    private static final String USER_NAME = "userName";
    private static final String USER_DESCRIPTION = "userDescription";
    private static final String FIRST_TWEET_ENTITY_URL = "firstTweetEntityUrl";
    private static final String SECOND_TWEET_ENTITY_URL = "secondTweetEntityUrl";
    private static final String FIRST_TWEET_ENTITY_EXPANDED_URL = "firstTweetEntityExpandedUrl";
    private static final String SECOND_TWEET_ENTITY_EXPANDED_URL = "secondTweetEntityExpandedUrl";
    private static final String CREATED = "created";
    private static final String TEXT = "text";

    private Tweet tweet;

    @Before
    public void buildTweet() {
        final Urls firstUserEntityUrls = new Urls(FIRST_USER_ENTITY_URL, FIRST_USER_ENTITY_EXPANDED_URL);
        final Urls secondUserEntityUrls = new Urls(SECOND_USER_ENTITY_URL, SECOND_USER_ENTITY_EXPANDED_URL);
        final List<Urls> userEntityUrls = new ArrayList<Urls>() {{
            add(firstUserEntityUrls);
            add(secondUserEntityUrls);
        }};
        final Url userEntityUrl = new Url(userEntityUrls);
        final UserEntities userEntities = new UserEntities(userEntityUrl);
        final User user = new User(USER_NAME, USER_DESCRIPTION, userEntities);

        final Urls firstTweetEntityUrl = new Urls(FIRST_TWEET_ENTITY_URL, FIRST_TWEET_ENTITY_EXPANDED_URL);
        final Urls secondTweetEntityUrl = new Urls(SECOND_TWEET_ENTITY_URL, SECOND_TWEET_ENTITY_EXPANDED_URL);
        final List<Urls> tweetEntitiesUrls = new ArrayList<Urls>() {{
            add(firstTweetEntityUrl);
            add(secondTweetEntityUrl);
        }};
        final TweetEntities tweetEntities = new TweetEntities(tweetEntitiesUrls);

        this.tweet = new Tweet(CREATED, TEXT, user, tweetEntities);
    }

    @Test
    public void whenUserIsNullThenNullReturns() {
        this.tweet.setUser(null);

        assertNull(convertToHeader(this.tweet));
    }

    @Test
    public void whenUserNameNullDescriptionNullThenNullReturns() {
        this.tweet.getUser().setName(null);
        this.tweet.getUser().setDescription(null);

        assertNull(convertToHeader(this.tweet));
    }

    @Test
    public void whenUserNameEmptyDescriptionNullThenNullReturns() {
        this.tweet.getUser().setName("");
        this.tweet.getUser().setDescription(null);

        assertNull(convertToHeader(this.tweet));
    }

    @Test
    public void whenUserNameEmptyDescriptionEmptyThenNullReturns() {
        this.tweet.getUser().setName("");
        this.tweet.getUser().setDescription("");

        assertNull(convertToHeader(this.tweet));
    }

    @Test
    public void whenUserNameNullDescriptionEmptyThenNullReturns() {
        this.tweet.getUser().setName(null);
        this.tweet.getUser().setDescription("");

        assertNull(convertToHeader(this.tweet));
    }

    @Test
    public void whenUserNameExistDescriptionNullThenNameCopiesToDescription() {
        this.tweet.getUser().setDescription(null);

        final FeedHeader feedHeader = convertToHeader(this.tweet);
        assertEquals(USER_NAME, feedHeader.description);
    }

}
