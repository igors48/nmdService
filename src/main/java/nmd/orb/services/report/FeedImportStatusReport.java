package nmd.orb.services.report;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.isPositive;

/**
 * @author : igu
 */
public class FeedImportStatusReport {

    public static final FeedImportStatusReport DEFAULT = new FeedImportStatusReport(0, 0, 0);

    private final int scheduled;
    private final int imported;
    private final int failed;

    public FeedImportStatusReport(final int scheduled, final int imported, final int failed) {
        guard(isPositive(scheduled));
        this.scheduled = scheduled;

        guard(isPositive(imported));
        this.imported = imported;

        guard(isPositive(failed));
        this.failed = failed;
    }

    public int getScheduled() {
        return this.scheduled;
    }

    public int getImported() {
        return this.imported;
    }

    public int getFailed() {
        return this.failed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FeedImportStatusReport report = (FeedImportStatusReport) o;

        if (failed != report.failed) return false;
        if (imported != report.imported) return false;
        if (scheduled != report.scheduled) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = scheduled;
        result = 31 * result + imported;
        result = 31 * result + failed;
        return result;
    }
}
