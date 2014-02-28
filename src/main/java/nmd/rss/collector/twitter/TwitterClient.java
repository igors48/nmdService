package nmd.rss.collector.twitter;

import nmd.rss.collector.twitter.entities.AccessToken;
import nmd.rss.collector.twitter.entities.Tweet;

import java.io.IOException;
import java.util.List;

import static nmd.rss.collector.util.Assert.assertPositive;
import static nmd.rss.collector.util.Assert.assertStringIsValid;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 25.02.14
 */
public class TwitterClient {

    private final String apiKey;
    private final String apiSecret;

    public TwitterClient(final String apiKey, final String apiSecret) {
        assertStringIsValid(apiKey);
        this.apiKey = apiKey;

        assertStringIsValid(apiSecret);
        this.apiSecret = apiSecret;
    }

    public List<Tweet> fetchTweets(final String screenName, final int count) throws IOException {
        assertStringIsValid(screenName);
        assertPositive(count);

        final AccessToken accessToken = TwitterTools.getAccessToken(this.apiKey, this.apiSecret);

        return TwitterTools.fetchTweets(accessToken, screenName, count);
    }

}
