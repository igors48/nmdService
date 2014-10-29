package nmd.orb.services.report;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static nmd.orb.util.Assert.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date: 18.12.13
 */
public class FeedItemsReport {

    public final UUID id;
    public final String title;
    public final int read;
    public final int notRead;
    public final int readLater;
    public final List<FeedItemReport> reports;
    public final Date lastUpdate;


    public FeedItemsReport(final UUID id, final String title, final int read, final int notRead, final int readLater, final List<FeedItemReport> reports, final Date lastUpdate) {
        assertNotNull(id);
        this.id = id;

        assertStringIsValid(title);
        this.title = title;

        assertPositive(read);
        this.read = read;

        assertPositive(notRead);
        this.notRead = notRead;

        assertPositive(readLater);
        this.readLater = readLater;

        assertNotNull(reports);
        this.reports = reports;

        assertNotNull(lastUpdate);
        this.lastUpdate = lastUpdate;
    }

}