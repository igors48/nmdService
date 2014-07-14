package nmd.rss.collector.twitter;

import nmd.rss.collector.feed.Feed;
import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.collector.feed.FeedItem;
import nmd.rss.collector.twitter.entities.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static nmd.rss.collector.feed.FeedHeader.MAX_DESCRIPTION_AND_TITLE_LENGTH;
import static nmd.rss.collector.feed.FeedHeader.create;
import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.collector.util.Assert.assertStringIsValid;
import static nmd.rss.collector.util.StringTools.cutTo;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 28.02.14
 */
public final class TweetConversionTools {

    private static final SimpleDateFormat TWITTER_DATE_PARSER;

    static {
        TWITTER_DATE_PARSER = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.ENGLISH);
        TWITTER_DATE_PARSER.setLenient(true);
    }

    private TweetConversionTools() {
        // empty
    }

    public static FeedHeader convertToHeader(final String twitterLink, final Tweet tweet) {
        assertStringIsValid(twitterLink);
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

        final Url url = getUrl(entities);
        final Urls first = getFirstUrl(url);
        final String expandedFirstUrl = getExpandedFirstUrl(first);

        final String feedLink = isBlank(expandedFirstUrl) ? twitterLink : expandedFirstUrl;

        final UUID id = UUID.randomUUID();

        return create(id, twitterLink.trim(), cutTo(title, MAX_DESCRIPTION_AND_TITLE_LENGTH), cutTo(description, MAX_DESCRIPTION_AND_TITLE_LENGTH), feedLink);
    }

    private static String getExpandedFirstUrl(Urls first) {
        if (first == null) return null;

        final String expandedUrl = first.getExpanded_url();

        if (isBlank(expandedUrl)) {
            return null;
        }

        return expandedUrl.trim();
    }

    private static Urls getFirstUrl(Url url) {
        if (url == null) return null;

        final List<Urls> urls = url.getUrls();

        if (urls == null || urls.isEmpty()) {
            return null;
        }

        return urls.get(0);
    }

    private static Url getUrl(UserEntities entities) {
        if (entities == null) {
            return null;
        }

        return entities.getUrl();
    }

    public static FeedItem convertToItem(final Tweet tweet, final Date current) {
        assertNotNull(tweet);
        assertNotNull(current);

        final String text = tweet.getText();

        if (isBlank(text)) {
            return null;
        }

        final String title = text.trim();

        final TweetEntities entities = tweet.getEntities();
        final String expandedUrl = getLinkFromTweetEntities(entities);

        if (expandedUrl == null) {
            return null;
        }

        final String link = /*isBlank(expandedUrl) ? createTweetUrl(tweet.getUser().getName(), tweet.getId_str()) :*/ expandedUrl;

        final String dateAsString = tweet.getCreated_at();

        final boolean dateReal;

        final Date date = parse(dateAsString);
        final Date itemDate;

        if (date == null) {
            dateReal = false;
            itemDate = current;
        } else {
            dateReal = true;
            itemDate = date;
        }

        final String id = UUID.randomUUID().toString();

        return new FeedItem(title, title, link, itemDate, dateReal, id);
    }

    private static String getLinkFromTweetEntities(final TweetEntities entities) {

        if (entities == null) {
            return null;
        }

        final List<Urls> urls = entities.getUrls();

        if (urls == null || urls.isEmpty()) {
            return null;
        }

        final Urls first = urls.get(0);

        if (first == null) {
            return null;
        }

        final String expandedUrl = first.getExpanded_url();

        if (isBlank(expandedUrl)) {
            return null;
        }

        return expandedUrl.trim();
    }

    private static String createTweetUrl(final String userName, final String tweetIdAsString) {
        return null;
    }

    public static Feed convertToFeed(final String twitterLink, final List<Tweet> tweets, final Date current) {
        assertNotNull(current);

        if (tweets == null || tweets.isEmpty()) {
            return null;
        }

        FeedHeader feedHeader = null;
        final List<FeedItem> feedItems = new ArrayList<>();

        for (final Tweet tweet : tweets) {

            if (tweet != null) {
                feedHeader = convertToHeader(twitterLink, tweets.get(0));
                final FeedItem feedItem = convertToItem(tweet, current);

                if (feedItem != null) {
                    feedItems.add(feedItem);
                }
            }
        }

        if (feedHeader == null) {
            return null;
        }

        return new Feed(feedHeader, feedItems);
    }

    public static Date parse(final String dateAsString) {

        try {

            if (isBlank(dateAsString)) {
                return null;
            }

            return TWITTER_DATE_PARSER.parse(dateAsString);
        } catch (ParseException e) {
            return null;
        }
    }

    private static boolean isBlank(final String string) {
        return string == null || string.trim().isEmpty();
    }

}
