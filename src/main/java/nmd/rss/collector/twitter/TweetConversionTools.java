package nmd.rss.collector.twitter;

import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.collector.twitter.entities.Tweet;
import nmd.rss.collector.twitter.entities.TweetEntities;
import nmd.rss.collector.twitter.entities.Urls;
import nmd.rss.collector.twitter.entities.User;

import java.util.List;
import java.util.UUID;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 28.02.14
 */
public class TweetConversionTools {

    public static FeedHeader convertToHeader(final Tweet tweet) {
        assertNotNull(tweet);

        final User user = tweet.getUser();

        if (user == null) {
            return null;
        }

        final String userName = user.getName();
        final String userDescription = user.getDescription();

        if (isBlank(userName) && isBlank(userDescription)) {
            return null;
        }

        final String title = isBlank(userName) ? userDescription : userName;
        final String description = isBlank(userDescription) ? userName : userDescription;

        final TweetEntities entities = tweet.getEntities();

        if (entities == null) {
            return null;
        }

        final List<Urls> urls = entities.getUrls();

        if (urls == null || urls.isEmpty()) {
            return null;
        }

        final String feedLink = urls.get(0).getExpanded_url();

        if (isBlank(feedLink)) {
            return null;
        }

        final UUID id = UUID.randomUUID();

        return new FeedHeader(id, feedLink, title, description, feedLink);
    }

    private static boolean isBlank(final String string) {
        return string == null || string.isEmpty();
    }

    private TweetConversionTools() {
        // empty
    }

}
