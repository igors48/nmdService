package nmd.orb.services.report;

import java.util.Date;
import java.util.UUID;

import static nmd.orb.util.Assert.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date: 22.10.13
 */
public class FeedReadReport {

    public final UUID feedId;
    public final FeedType feedType;
    public final String feedTitle;
    public final int read;
    public final int notRead;
    public final int readLater;
    public final int addedFromLastVisit;
    public final String topItemId;
    public final String topItemLink;
    public final Date lastUpdate;

    public FeedReadReport(final UUID feedId, final FeedType feedType, final String feedTitle, final int read, final int notRead, final int readLater, final int addedFromLastVisit, final String topItemId, final String topItemLink, final Date lastUpdate) {
        assertNotNull(feedId);
        this.feedId = feedId;

        assertNotNull(feedType);
        this.feedType = feedType;

        assertStringIsValid(feedTitle);
        this.feedTitle = feedTitle;

        assertPositive(read);
        this.read = read;

        assertPositive(notRead);
        this.notRead = notRead;

        assertPositive(readLater);
        this.readLater = readLater;

        assertPositive(addedFromLastVisit);
        this.addedFromLastVisit = addedFromLastVisit;

        this.topItemId = topItemId;

        this.topItemLink = topItemLink;

        assertNotNull(lastUpdate);
        this.lastUpdate = lastUpdate;
    }

}