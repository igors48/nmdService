package nmd.orb.sources.twitter;

import nmd.orb.sources.twitter.entities.AccessToken;
import nmd.orb.sources.twitter.entities.Tweet;

import java.io.IOException;
import java.util.List;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.isPositive;
import static nmd.orb.util.Parameter.isValidString;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 25.02.14
 */
public class TwitterClient {

    private final String apiKey;
    private final String apiSecret;

    public TwitterClient(final String apiKey, final String apiSecret) {
        guard(isValidString(apiKey));
        this.apiKey = apiKey;

        guard(isValidString(apiSecret));
        this.apiSecret = apiSecret;
    }

    public List<Tweet> fetchTweets(final String screenName, final int count) throws IOException {
        guard(isValidString(screenName));
        guard(isPositive(count));

        final AccessToken accessToken = TwitterClientTools.getAccessToken(this.apiKey, this.apiSecret);

        return TwitterClientTools.fetchTweets(accessToken, screenName, count);
    }

}
