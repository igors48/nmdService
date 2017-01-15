package nmd.orb.services.report;

import java.util.ArrayList;
import java.util.List;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 15.03.14s
 */
public class CategoryReport {

    public final String id;
    public final String name;
    public final List<FeedReadReport> feedReadReports;
    public final int read;
    public final int notRead;
    public final int readLater;

    public CategoryReport(final String id, final String name, final List<FeedReadReport> feedReadReports, final int read, final int notRead, final int readLater) {
        guard(isValidString(this.id = id));
        guard(isValidString(this.name = name));
        guard(notNull(this.feedReadReports = feedReadReports));
        guard(isPositive(this.read = read));
        guard(isPositive(this.notRead = notRead));
        guard(isPositive(this.readLater = readLater));
    }

    public static CategoryReport create(final String id, final String name) {
        return new CategoryReport(id, name, new ArrayList<FeedReadReport>(), 0, 0, 0);
    }

}