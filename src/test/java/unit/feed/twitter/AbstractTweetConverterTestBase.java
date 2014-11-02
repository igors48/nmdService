package unit.feed.twitter;

import nmd.orb.sources.twitter.TweetConversionTools;
import nmd.orb.sources.twitter.entities.*;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 02.03.14
 */
public abstract class AbstractTweetConverterTestBase {

    protected static final String FIRST_USER_ENTITY_EXPANDED_URL = "http://domain.com/firstUserEntityExpandedUrl";
    protected static final String USER_NAME = "userName";
    protected static final String USER_SCREEN_NAME = "userScreenName";
    protected static final String USER_DESCRIPTION = "userDescription";
    protected static final String FIRST_TWEET_ENTITY_EXPANDED_URL = "http://domain.com/firstTweetEntityExpandedUrl";
    protected static final String CREATED = "Thu Dec 23 18:26:07 +0000 2010";
    protected static final String TEXT = "text";

    private static final String TWEET_ID_AS_STRING = "48";
    private static final String FIRST_USER_ENTITY_URL = "http://domain.com/firstUserEntityUrl";
    private static final String SECOND_USER_ENTITY_URL = "http://domain.com/secondUserEntityUrl";
    private static final String SECOND_USER_ENTITY_EXPANDED_URL = "http://domain.com/secondUserEntityExpandedUrl";
    private static final String FIRST_TWEET_ENTITY_URL = "http://domain.com/firstTweetEntityUrl";
    private static final String SECOND_TWEET_ENTITY_URL = "http://domain.com/secondTweetEntityUrl";
    private static final String SECOND_TWEET_ENTITY_EXPANDED_URL = "http://domain.com/secondTweetEntityExpandedUrl";

    protected Tweet tweet;
    protected List<Tweet> tweets;

    @Before
    public void before() {
        final Urls firstUserEntityUrls = new Urls(FIRST_USER_ENTITY_URL + " ", FIRST_USER_ENTITY_EXPANDED_URL + " ");
        final Urls secondUserEntityUrls = new Urls(SECOND_USER_ENTITY_URL + " ", SECOND_USER_ENTITY_EXPANDED_URL + " ");
        final List<Urls> userEntityUrls = new ArrayList<Urls>() {{
            add(firstUserEntityUrls);
            add(secondUserEntityUrls);
        }};
        final Url userEntityUrl = new Url(userEntityUrls);
        final UserEntities userEntities = new UserEntities(userEntityUrl);
        final User user = new User(USER_NAME + " ", USER_SCREEN_NAME + " ", USER_DESCRIPTION + " ", userEntities);

        final Urls firstTweetEntityUrl = new Urls(FIRST_TWEET_ENTITY_URL + " ", FIRST_TWEET_ENTITY_EXPANDED_URL + " ");
        final Urls secondTweetEntityUrl = new Urls(SECOND_TWEET_ENTITY_URL + " ", SECOND_TWEET_ENTITY_EXPANDED_URL + " ");
        final List<Urls> tweetEntitiesUrls = new ArrayList<Urls>() {{
            add(firstTweetEntityUrl);
            add(secondTweetEntityUrl);
        }};
        final TweetEntities tweetEntities = new TweetEntities(tweetEntitiesUrls);

        this.tweet = new Tweet(TWEET_ID_AS_STRING, CREATED, TEXT, user, tweetEntities);
        this.tweets = new ArrayList<Tweet>() {{
            add(tweet);
        }};
    }

    protected String makeTweetUrl() {
        return TweetConversionTools.createTweetUrl(USER_SCREEN_NAME, TWEET_ID_AS_STRING);
    }
}
