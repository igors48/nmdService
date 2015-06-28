package nmd.orb.sources.twitter;

import nmd.orb.error.ServiceException;
import nmd.orb.feed.Feed;
import nmd.orb.sources.twitter.entities.AccessToken;
import nmd.orb.sources.twitter.entities.Tweet;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static nmd.orb.error.ServiceError.urlFetcherError;
import static nmd.orb.sources.twitter.TweetConversionTools.convertToFeed;
import static nmd.orb.sources.twitter.TwitterClientTools.*;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.isPositive;
import static nmd.orb.util.Parameter.isValidString;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 25.02.14
 */
public class TwitterClient {

    private static final String TWITTER_API_KEY = "twitter.apiKey";
    private static final String TWITTER_API_SECRET = "twitter.apiSecret";
    private static final int TWEETS_PER_FETCH = 100;

    public static Feed fetchAsTwitterUrl(final String twitterUrl) throws ServiceException {
        guard(isItTwitterUrl(twitterUrl));

        try {
            final String apiKey = System.getProperty(TWITTER_API_KEY);
            final String apiSecret = System.getProperty(TWITTER_API_SECRET);

            final String userName = getTwitterUserName(twitterUrl);
            final List<Tweet> tweets = fetchTweets(apiKey, apiSecret, userName, TWEETS_PER_FETCH);

            return convertToFeed(twitterUrl, tweets, new Date());
        } catch (IOException exception) {
            throw new ServiceException(urlFetcherError(twitterUrl), exception);
        }
    }


    public static List<Tweet> fetchTweets(final String apiKey, final String apiSecret, final String screenName, final int count) throws IOException {
        guard(isValidString(apiKey));
        guard(isValidString(apiSecret));
        guard(isValidString(screenName));
        guard(isPositive(count));

        final AccessToken accessToken = getAccessToken(apiKey, apiSecret);

        return TwitterClientTools.fetchTweets(accessToken, screenName, count);
    }

    private TwitterClient() {
        // empty
    }

}
