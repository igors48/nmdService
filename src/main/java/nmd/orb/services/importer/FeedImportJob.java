package nmd.orb.services.importer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * @author : igu
 */
public class FeedImportJob {

    private final UUID id; // TODO remove
    private final List<FeedImportTask> tasks;

    private FeedImportJobStatus status;

    public FeedImportJob(final UUID id, final FeedImportJobStatus status) {
        guard(notNull(id));
        this.id = id;

        guard(notNull(status));
        this.status = status;

        this.tasks = new ArrayList<>();
    }

    public UUID getId() {
        return this.id;
    }

    public void setStatus(final FeedImportJobStatus status) {
        guard(notNull(status));
        this.status = status;
    }

    public FeedImportJobStatus getStatus() {
        return this.status;
    }

    public FeedImportTask getCurrentTask() {
        return null;
    }

}
