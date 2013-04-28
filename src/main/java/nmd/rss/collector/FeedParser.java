package nmd.rss.collector;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static nmd.rss.collector.util.Assert.assertStringIsValid;
import static nmd.rss.collector.util.Parameter.isValidUrl;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 28.04.13
 */
public final class FeedParser {

    public static ParsedFeed parse(final String data) throws FeedParserException {
        assertStringIsValid(data);

        try {
            StringReader reader = new StringReader(data);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(reader);

            FeedHeader header = parseHeader(feed);

            if (header == null) {
                return null;
            }

            List<FeedItem> items = new ArrayList<>();

            for (int i = 0; i < feed.getEntries().size(); i++) {
                SyndEntry entry = (SyndEntry) feed.getEntries().get(i);
                FeedItem item = parseItem(entry);

                if (item != null) {
                    items.add(item);
                }
            }

            return new ParsedFeed(header, items);
        } catch (FeedException exception) {
            throw new FeedParserException(exception);
        }

    }

    private static FeedHeader parseHeader(final SyndFeed feed) {

        if (!isValidUrl(feed.getLink())) {
            return null;
        }

        String title = createTitle(feed.getTitle(), feed.getLink());
        String description = feed.getDescription() == null ? "" : feed.getDescription();
        String link = feed.getLink();

        return new FeedHeader(title, description, link);
    }

    private static FeedItem parseItem(final SyndEntry entry) {

        if (!isValidUrl(entry.getLink())) {
            return null;
        }

        String title = createTitle(entry.getTitle(), entry.getLink());
        String description = createDescription(entry);
        String link = entry.getLink();
        long timestamp = createTimestamp(entry.getPublishedDate(), new Date());

        return new FeedItem(title, description, link, timestamp);
    }

    private static long createTimestamp(final Date date, final Date currentDate) {
        return date == null ? currentDate.getTime() : date.getTime();
    }

    private static String createTitle(final String title, final String feedLink) {
        return (title == null) || (title.isEmpty()) ? feedLink : title;
    }

    private static String createDescription(final SyndEntry entry) {
        String title = entry.getTitle();
        List contentsList = entry.getContents();
        StringBuilder contents = new StringBuilder();

        if (contentsList != null && !contentsList.isEmpty()) {

            for (Object current : contentsList) {
                contents.append(((SyndContent) current).getValue());
            }
        }

        String description = entry.getDescription() == null ? "" : entry.getDescription().getValue();

        String result = contents.length() == 0 ? description : contents.toString();

        return result.isEmpty() ? title : result;
    }


    private FeedParser() {
        // empty
    }

}
