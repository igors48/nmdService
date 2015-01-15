package nmd.orb.sources.twitter;

import nmd.orb.feed.Feed;
import nmd.orb.feed.FeedHeader;
import nmd.orb.feed.FeedItem;
import nmd.orb.sources.twitter.entities.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.String.format;
import static nmd.orb.feed.FeedHeader.MAX_DESCRIPTION_AND_TITLE_LENGTH;
import static nmd.orb.feed.FeedHeader.create;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.*;
import static nmd.orb.util.StringTools.cutTo;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 28.02.14
 */
public final class TweetConversionTools {

    public static final SimpleDateFormat TWITTER_DATE_PARSER;

    private static final String TWEET_URL_TEMPLATE = "https://twitter.com/%s/status/%s";

    static {
        TWITTER_DATE_PARSER = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.ENGLISH);
        TWITTER_DATE_PARSER.setLenient(true);
    }

    private TweetConversionTools() {
        // empty
    }

    public static FeedHeader convertToHeader(final String twitterLink, final Tweet tweet) {
        guard(isValidUrl(twitterLink));
        guard(notNull(tweet));

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

    public static FeedItem convertToItem(final Tweet tweet, final Date current) {
        guard(notNull(tweet));
        guard(notNull(current));

        final String text = tweet.getText();

        if (isBlank(text)) {
            return null;
        }

        final String title = text.trim();

        final TweetEntities entities = tweet.getEntities();
        final String expandedUrl = getLinkFromTweetEntities(entities);

        final String link = createTweetUrl(tweet.getUser().getScreen_name(), tweet.getId_str());
        final String gotoLink = isBlank(expandedUrl) ? link : expandedUrl;

        final String dateAsString = tweet.getCreated_at();

        final Date date = parse(dateAsString);
        final boolean dateReal = FeedItem.isDateReal(date, current);
        final Date itemDate = dateReal ? date : current;

        final String id = UUID.randomUUID().toString();

        return new FeedItem(title, title, link, gotoLink, itemDate, dateReal, id);
    }

    public static String createTweetUrl(final String userName, final String tweetIdAsString) {
        guard(isValidString(userName));
        guard(isValidString(tweetIdAsString));

        return format(TWEET_URL_TEMPLATE, userName.trim(), tweetIdAsString.trim());
    }

    public static Feed convertToFeed(final String twitterLink, final List<Tweet> tweets, final Date current) {
        guard(notNull(current));

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

    private static String getExpandedFirstUrl(final Urls first) {

        if (first == null) return null;

        final String expandedUrl = first.getExpanded_url();

        if (isBlank(expandedUrl)) {
            return null;
        }

        return expandedUrl.trim();
    }

    private static Urls getFirstUrl(final Url url) {

        if (url == null) return null;

        final List<Urls> urls = url.getUrls();

        if (urls == null || urls.isEmpty()) {
            return null;
        }

        return urls.get(0);
    }

    private static Url getUrl(final UserEntities entities) {

        if (entities == null) {
            return null;
        }

        return entities.getUrl();
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

    private static boolean isBlank(final String string) {
        return string == null || string.trim().isEmpty();
    }

}
