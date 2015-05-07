package nmd.orb.services.report;

import nmd.orb.sources.Source;

import java.util.Date;
import java.util.UUID;

import static nmd.orb.util.Assert.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date: 22.10.13
 */
public class FeedReadReport {

    public final UUID feedId;
    public final Source feedType;
    public final String feedTitle;
    public final int read;
    public final int notRead;
    public final int readLater;
    public final int addedFromLastVisit;
    public final String topItemId;
    public final String topItemLink;
    public final Date lastUpdate;
    public final boolean hasErrors;

    public FeedReadReport(final UUID feedId, final Source feedType, final String feedTitle, final int read, final int notRead, final int readLater, final int addedFromLastVisit, final String topItemId, final String topItemLink, final Date lastUpdate, final boolean hasErrors) {
        assertNotNull(this.feedId = feedId);
        assertNotNull(this.feedType = feedType);
        assertStringIsValid(this.feedTitle = feedTitle);
        assertPositive(this.read = read);
        assertPositive(this.notRead = notRead);
        assertPositive(this.readLater = readLater);
        assertPositive(this.addedFromLastVisit = addedFromLastVisit);
        this.topItemId = topItemId;
        this.topItemLink = topItemLink;
        assertNotNull(this.lastUpdate = lastUpdate);
        this.hasErrors = hasErrors;
    }

}
