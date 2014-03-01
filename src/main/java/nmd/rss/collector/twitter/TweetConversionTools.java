package nmd.rss.collector.twitter;

import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.collector.feed.FeedItem;
import nmd.rss.collector.twitter.entities.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 28.02.14
 */
public class TweetConversionTools {

    private static final SimpleDateFormat TWITTER_DATE_PARSER;

    static {
        TWITTER_DATE_PARSER = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.ENGLISH);
        TWITTER_DATE_PARSER.setLenient(true);
    }

    private TweetConversionTools() {
        // empty
    }

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

        final Urls first = urls.get(0);

        if (first == null) {
            return null;
        }

        final String expandedUrl = first.getExpanded_url();

        if (isBlank(expandedUrl)) {
            return null;
        }

        final String feedLink = expandedUrl.trim();

        final UUID id = UUID.randomUUID();

        return new FeedHeader(id, feedLink, title, description, feedLink);
    }

    public static FeedItem convertToItem(final Tweet tweet, final Date current) {
        assertNotNull(tweet);

        final String text = tweet.getText();

        if (isBlank(text)) {
            return null;
        }

        final String title = text.trim();

        final TweetEntities entities = tweet.getEntities();

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

        final String link = expandedUrl.trim();

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

    private static Date parse(final String dateAsString) {

        try {
            return TWITTER_DATE_PARSER.parse(dateAsString);
        } catch (ParseException e) {
            return null;
        }
    }

    private static boolean isBlank(final String string) {
        return string == null || string.trim().isEmpty();
    }

}
