package unit.feed.twitter;

import nmd.rss.collector.twitter.entities.*;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 02.03.14
 */
public abstract class AbstractTweetConverterTestBase {

    protected static final String FIRST_USER_ENTITY_EXPANDED_URL = "firstUserEntityExpandedUrl";
    protected static final String USER_NAME = "userName";
    protected static final String USER_DESCRIPTION = "userDescription";
    protected static final String FIRST_TWEET_ENTITY_EXPANDED_URL = "firstTweetEntityExpandedUrl";
    protected static final String CREATED = "Thu Dec 23 18:26:07 +0000 2010";
    protected static final String TEXT = "text";

    private static final String FIRST_USER_ENTITY_URL = "firstUserEntityUrl";
    private static final String SECOND_USER_ENTITY_URL = "secondUserEntityUrl";
    private static final String SECOND_USER_ENTITY_EXPANDED_URL = "secondUserEntityExpandedUrl";
    private static final String FIRST_TWEET_ENTITY_URL = "firstTweetEntityUrl";
    private static final String SECOND_TWEET_ENTITY_URL = "secondTweetEntityUrl";
    private static final String SECOND_TWEET_ENTITY_EXPANDED_URL = "secondTweetEntityExpandedUrl";

    protected Tweet tweet;

    @Before
    public void buildTweet() {
        final Urls firstUserEntityUrls = new Urls(FIRST_USER_ENTITY_URL + " ", FIRST_USER_ENTITY_EXPANDED_URL + " ");
        final Urls secondUserEntityUrls = new Urls(SECOND_USER_ENTITY_URL + " ", SECOND_USER_ENTITY_EXPANDED_URL + " ");
        final List<Urls> userEntityUrls = new ArrayList<Urls>() {{
            add(firstUserEntityUrls);
            add(secondUserEntityUrls);
        }};
        final Url userEntityUrl = new Url(userEntityUrls);
        final UserEntities userEntities = new UserEntities(userEntityUrl);
        final User user = new User(USER_NAME + " ", USER_DESCRIPTION + " ", userEntities);

        final Urls firstTweetEntityUrl = new Urls(FIRST_TWEET_ENTITY_URL + " ", FIRST_TWEET_ENTITY_EXPANDED_URL + " ");
        final Urls secondTweetEntityUrl = new Urls(SECOND_TWEET_ENTITY_URL + " ", SECOND_TWEET_ENTITY_EXPANDED_URL + " ");
        final List<Urls> tweetEntitiesUrls = new ArrayList<Urls>() {{
            add(firstTweetEntityUrl);
            add(secondTweetEntityUrl);
        }};
        final TweetEntities tweetEntities = new TweetEntities(tweetEntitiesUrls);

        this.tweet = new Tweet(CREATED, TEXT, user, tweetEntities);
    }

}
