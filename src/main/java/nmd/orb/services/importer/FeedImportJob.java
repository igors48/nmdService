package nmd.orb.services.importer;

import java.util.UUID;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * @author : igu
 */
public class FeedImportJob {

    public UUID id; // TODO remove
    public FeedImportJobStatus status;

    public FeedImportJob(final UUID id, final FeedImportJobStatus status) {
        guard(notNull(id));
        this.id = id;

        guard(notNull(status));
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FeedImportJob job = (FeedImportJob) o;

        if (!id.equals(job.id)) return false;
        if (status != job.status) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + status.hashCode();
        return result;
    }
}
