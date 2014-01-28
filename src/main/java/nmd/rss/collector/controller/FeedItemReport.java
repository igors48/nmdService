package nmd.rss.collector.controller;

import java.util.Date;
import java.util.UUID;

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
    public final String itemId;
    public final boolean read;
    public final boolean readLater;

    public FeedItemReport(final UUID feedId, final String title, final String description, final String link, final Date date, final String itemId, final boolean read, final boolean readLater) {
        this.feedId = feedId;
        this.title = title;
        this.description = description;
        this.link = link;
        this.date = date;
        this.itemId = itemId;
        this.read = read;
        this.readLater = readLater;
    }

}
