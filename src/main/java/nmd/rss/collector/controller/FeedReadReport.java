package nmd.rss.collector.controller;

import java.util.UUID;

import static nmd.rss.collector.util.Assert.*;

/**
 * User: igu
 * Date: 22.10.13
 */
public class FeedReadReport {

    public final UUID feedId;
    public final String feedTitle;
    public final int read;
    public final int notRead;
    public final String topItemId;
    public final String topItemLink;

    public FeedReadReport(final UUID feedId, final String feedTitle, final int read, final int notRead, final String topItemId, final String topItemLink) {
        assertNotNull(feedId);
        this.feedId = feedId;

        assertStringIsValid(feedTitle);
        this.feedTitle = feedTitle;

        assertPositive(read);
        this.read = read;

        assertPositive(notRead);
        this.notRead = notRead;

        this.topItemId = topItemId;

        this.topItemLink = topItemLink;
    }

}
