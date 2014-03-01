package nmd.rss.collector.twitter;

import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.collector.twitter.entities.*;

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

        final String title = (isBlank(userName) ? userDescription : userName).trim();
        final String description = (isBlank(userDescription) ? userName : userDescription).trim();

        final UserEntities entities = user.getEntities();

        if (entities == null) {
            return null;
        }

        final Url url = entities.getUrl();

        if (url == null) {
            return null;
        }

        final List<Urls> urls = url.getUrls();

        if (urls == null || urls.isEmpty()) {
            return null;
        }

        final String expandedUrl = urls.get(0).getExpanded_url();

        if (isBlank(expandedUrl)) {
            return null;
        }

        final String feedLink = expandedUrl.trim();

        final UUID id = UUID.randomUUID();

        return new FeedHeader(id, feedLink, title, description, feedLink);
    }

    private static boolean isBlank(final String string) {
        return string == null || string.trim().isEmpty();
    }

    private TweetConversionTools() {
        // empty
    }

}
