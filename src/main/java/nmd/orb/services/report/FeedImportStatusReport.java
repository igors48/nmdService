package nmd.orb.services.report;

import nmd.orb.services.importer.ImportJobStatus;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.isPositive;
import static nmd.orb.util.Parameter.notNull;

/**
 * @author : igu
 */
public class FeedImportStatusReport {

    public static final FeedImportStatusReport DEFAULT = new FeedImportStatusReport(ImportJobStatus.STOPPED, 0, 0, 0);

    private final ImportJobStatus status;
    private final int scheduled;
    private final int imported;
    private final int failed;

    public FeedImportStatusReport(final ImportJobStatus status, final int scheduled, final int imported, final int failed) {
        guard(notNull(status));
        this.status = status;

        guard(isPositive(scheduled));
        this.scheduled = scheduled;

        guard(isPositive(imported));
        this.imported = imported;

        guard(isPositive(failed));
        this.failed = failed;
    }

    public ImportJobStatus getStatus() {
        return this.status;
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

        FeedImportStatusReport that = (FeedImportStatusReport) o;

        if (failed != that.failed) return false;
        if (imported != that.imported) return false;
        if (scheduled != that.scheduled) return false;
        if (status != that.status) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = status.hashCode();
        result = 31 * result + scheduled;
        result = 31 * result + imported;
        result = 31 * result + failed;
        return result;
    }
}
