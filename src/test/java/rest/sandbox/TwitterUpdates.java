package rest.sandbox;

import nmd.rss.collector.feed.Feed;
import nmd.rss.collector.feed.FeedItem;
import nmd.rss.collector.feed.FeedItemsMergeReport;
import nmd.rss.collector.feed.FeedItemsMerger;
import nmd.rss.collector.twitter.TwitterClient;
import nmd.rss.collector.twitter.entities.Tweet;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static nmd.rss.collector.twitter.TweetConversionTools.convertToFeed;
import static nmd.rss.collector.twitter.TwitterClientTools.getTwitterUserName;

/**
 * @author : igu
 */
public class TwitterUpdates {

    @Test
    public void smoke() throws Exception {
        final Feed firstFeed = createFeed();
        final Feed secondFeed = createFeed();

        List<FeedItem> olds = firstFeed.items;
        List<FeedItem> news = secondFeed.items;

        final FeedItemsMergeReport mergeReport = FeedItemsMerger.merge(olds, news, 1000);

        System.out.println(mergeReport);
    }

    private Feed createFeed() throws IOException {
        final TwitterClient client = new TwitterClient("tjOc6yZT0a0QzxOLEpqGg", "avqQAxuOVlpHm09YsukVxfdIBAlhjVRqbWmVzJ1yVgs");
        String twitterUrl = "https://twitter.com/royksopp";
        final String userName = getTwitterUserName(twitterUrl);
        final List<Tweet> tweets = client.fetchTweets(userName, 1000);

        return convertToFeed(twitterUrl, tweets, new Date());
    }
}
