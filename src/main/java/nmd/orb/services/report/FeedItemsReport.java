package nmd.orb.services.report;

import nmd.orb.error.ServiceError;

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
    public final List<ServiceError> errors;

    public FeedItemsReport(final UUID id, final String title, final String link, final int read, final int notRead, final int readLater, final int addedSinceLastView, final List<FeedItemReport> reports, final Date lastUpdate, final Date topItemDate, final List<ServiceError> errors) {
        guard(notNull(this.id = id));
        guard(isValidString(this.title = title));
        guard(isValidUrl(this.link = link));
        guard(isPositive(this.read = read));
        guard(isPositive(this.notRead = notRead));
        guard(isPositive(this.readLater = readLater));
        guard(isPositive(this.addedSinceLastView = addedSinceLastView));
        guard(notNull(this.reports = reports));
        guard(notNull(this.lastUpdate = lastUpdate));
        guard(notNull(this.topItemDate = topItemDate));
        guard(notNull(this.errors = errors));
    }

}