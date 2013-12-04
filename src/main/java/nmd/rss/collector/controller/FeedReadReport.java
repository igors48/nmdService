package nmd.rss.collector.controller;

import java.util.UUID;

import static nmd.rss.collector.util.Assert.*;

/**
 * User: igu
 * Date: 22.10.13
 */
public class FeedReadReport {

    public final UUID feedId;
    public final String feedLink;
    public final int read;
    public final int notRead;
    //TODO add topItem (itemId + itemUrl);

    public FeedReadReport(final UUID feedId, final String feedLink, final int read, final int notRead) {
        assertNotNull(feedId);
        this.feedId = feedId;

        assertValidUrl(feedLink);
        this.feedLink = feedLink;

        assertPositive(read);
        this.read = read;

        assertPositive(notRead);
        this.notRead = notRead;
    }

}
