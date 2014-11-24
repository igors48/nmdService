package nmd.orb.services.report;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date: 18.12.13
 */
public class FeedItemsReport {

    public final UUID id;
    public final String title;
    public final String link;
    public final int read;
    public final int notRead;
    public final int readLater;
    public final int addedSinceLastView;
    public final List<FeedItemReport> reports;
    public final Date lastUpdate;
    public final Date topItemDate;

    public FeedItemsReport(final UUID id, final String title, final String link, final int read, final int notRead, final int readLater, final int addedSinceLastView, final List<FeedItemReport> reports, final Date lastUpdate, final Date topItemDate) {
        guard(notNull(id));
        this.id = id;

        guard(isValidString(title));
        this.title = title;

        guard(isValidUrl(link));
        this.link = link;

        guard(isPositive(read));
        this.read = read;

        guard(isPositive(notRead));
        this.notRead = notRead;

        guard(isPositive(readLater));
        this.readLater = readLater;

        guard(isPositive(addedSinceLastView));
        this.addedSinceLastView = addedSinceLastView;

        guard(notNull(reports));
        this.reports = reports;

        guard(notNull(lastUpdate));
        this.lastUpdate = lastUpdate;

        guard(notNull(topItemDate));
        this.topItemDate = topItemDate;
    }

}