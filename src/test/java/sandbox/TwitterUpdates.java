package sandbox;

import nmd.orb.collector.feed.Feed;
import nmd.orb.collector.feed.FeedItem;
import nmd.orb.collector.feed.FeedItemsMergeReport;
import nmd.orb.collector.feed.FeedItemsMerger;
import nmd.orb.sources.twitter.TwitterClient;
import nmd.orb.sources.twitter.entities.Tweet;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static nmd.orb.sources.twitter.TweetConversionTools.convertToFeed;
import static nmd.orb.sources.twitter.TwitterClientTools.getTwitterUserName;

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
        final List<FeedItem> addedAndRetained = mergeReport.getAddedAndRetained();

        final boolean theSame = FeedItemsMerger.listEqualsIgnoringGuid(addedAndRetained, olds);

        System.out.println(theSame);
    }

    private Feed createFeed() throws IOException {
        final TwitterClient client = new TwitterClient("*", "*");
        String twitterUrl = "https://twitter.com/royksopp";
        final String userName = getTwitterUserName(twitterUrl);
        final List<Tweet> tweets = client.fetchTweets(userName, 1000);

        return convertToFeed(twitterUrl, tweets, new Date());
    }
}
