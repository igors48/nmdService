package nmd.rss.collector.controller;

import nmd.rss.collector.feed.FeedItem;

import java.util.Date;
import java.util.UUID;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * User: igu
 * Date: 13.12.13
 */
public class FeedItemReport {

    public final UUID feedId;
    public final String title;
    public final String description;
    public final String link;
    public final Date date;
    public final String guid;
    public final boolean read;

    private FeedItemReport(final UUID feedId, final String title, final String description, final String link, final Date date, final String guid, final boolean read) {
        this.feedId = feedId;
        this.title = title;
        this.description = description;
        this.link = link;
        this.date = date;
        this.guid = guid;
        this.read = read;
    }

    public static FeedItemReport asRead(final UUID feedId, final FeedItem feedItem) {
        assertNotNull(feedId);
        assertNotNull(feedItem);

        return new FeedItemReport(feedId, feedItem.title, feedItem.description, feedItem.link, feedItem.date, feedItem.guid, true);
    }

    public static FeedItemReport asNotRead(final UUID feedId, final FeedItem feedItem) {
        assertNotNull(feedId);
        assertNotNull(feedItem);

        return new FeedItemReport(feedId, feedItem.title, feedItem.description, feedItem.link, feedItem.date, feedItem.guid, false);
    }

}
