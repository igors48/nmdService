package nmd.orb.sources.rss;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import nmd.orb.error.ServiceException;
import nmd.orb.feed.Feed;
import nmd.orb.feed.FeedHeader;
import nmd.orb.feed.FeedItem;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static nmd.orb.error.ServiceError.invalidFeedUrl;
import static nmd.orb.feed.FeedHeader.create;
import static nmd.orb.util.Assert.*;
import static nmd.orb.util.StringTools.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 28.04.13
 */
public final class FeedParser {

    private FeedParser() {
        // empty
    }

    public static Feed parse(final String feedUrl, final String feedData) throws FeedParserException {
        assertValidUrl(feedUrl);
        assertStringIsValid(feedData);

        try {
            final String correctedData = stripNonValidXMLCharacters(feedData);
            final StringReader reader = new StringReader(correctedData);
            final SyndFeedInput input = new SyndFeedInput();
            final SyndFeed feed = input.build(reader);

            final FeedHeader header = build(feedUrl, feed);

            if (header == null) {
                return null;
            }

            final List<FeedItem> items = new ArrayList<>();

            for (int i = 0; i < feed.getEntries().size(); i++) {
                final SyndEntry entry = (SyndEntry) feed.getEntries().get(i);
                final FeedItem item = build(entry);

                if (item != null) {
                    items.add(item);
                }
            }

            return new Feed(header, items);
        } catch (Exception exception) {
            throw new FeedParserException(exception);
        }

    }

    public static FeedItem build(final String link, final String title, final String description, final String alternateDescription, final Date date, final Date currentDate, final String guid) {
        assertNotNull(currentDate);
        assertNotNull(alternateDescription);
        assertStringIsValid(guid);

        final String itemLink = trim(link);

        if (itemLink.isEmpty()) {
            return null;
        }

        final String itemTitle = cutTo(trimOrUse(title, itemLink), FeedItem.MAX_TITLE_LENGTH);
        final String itemDescription = cutTo(trimOrUse(description, alternateDescription), FeedItem.MAX_DESCRIPTION_LENGTH);
        final boolean itemDateReal = FeedItem.isDateReal(date, currentDate);
        final Date feedDate = itemDateReal ? date : currentDate;

        return new FeedItem(itemTitle, itemDescription, itemLink, itemLink, feedDate, itemDateReal, guid);
    }

    public static FeedHeader build(final String url, final String link, final String title, final String description, final UUID guid) throws ServiceException {
        assertNotNull(guid);

        final String feedUrl = trim(url);

        if (feedUrl.isEmpty()) {
            throw new ServiceException(invalidFeedUrl(""));
        }

        final String feedLink = trimOrUse(link, feedUrl);
        final String feedDescription = cutTo(trimOrUse(description, feedUrl), FeedHeader.MAX_DESCRIPTION_AND_TITLE_LENGTH);
        final String feedTitle = cutTo(trimOrUse(title, feedUrl), FeedHeader.MAX_DESCRIPTION_AND_TITLE_LENGTH);

        return create(guid, feedUrl, feedTitle, feedDescription, feedLink);
    }

    private static FeedHeader build(final String feedUrl, final SyndFeed feed) throws ServiceException {
        final String feedLink = feed.getLink();
        final String feedTitle = feed.getTitle();
        final String feedDescription = feed.getDescription();
        final UUID feedGuid = UUID.randomUUID();

        return build(feedUrl, feedLink, feedTitle, feedDescription, feedGuid);
    }

    private static FeedItem build(final SyndEntry entry) {
        final String itemLink = entry.getLink();
        final String itemTitle = entry.getTitle();
        final String itemDescription = entry.getDescription() == null ? "" : entry.getDescription().getValue();
        final String itemAlternateDescription = createAlternateDescription(entry);
        final Date itemDate = entry.getPublishedDate();
        final Date itemCurrentDate = new Date();
        final String itemGuid = UUID.randomUUID().toString();

        return build(itemLink, itemTitle, itemDescription, itemAlternateDescription, itemDate, itemCurrentDate, itemGuid);
    }

    private static String createAlternateDescription(SyndEntry entry) {
        final List contentsList = entry.getContents();
        final StringBuilder contents = new StringBuilder();

        if (contentsList != null && !contentsList.isEmpty()) {

            for (final Object current : contentsList) {
                contents.append(((SyndContent) current).getValue());
            }
        }
        return contents.toString().trim();
    }

    private static String stripNonValidXMLCharacters(final String data) {
        final StringBuilder result = new StringBuilder();

        for (int i = 0; i < data.length(); i++) {
            final char current = data.charAt(i);

            if ((current == 0x9) || (current == 0xA) || (current == 0xD) ||
                    ((current >= 0x20) && (current <= 0xD7FF)) ||
                    ((current >= 0xE000) && (current <= 0xFFFD)) ||
                    ((current >= 0x10000) && (current <= 0x10FFFF))) {
                result.append(current);
            }
        }

        return result.toString();
    }

}
