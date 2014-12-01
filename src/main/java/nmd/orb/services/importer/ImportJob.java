package nmd.orb.services.importer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * @author : igu
 */
public class ImportJob {

    private final UUID id; // TODO remove
    private final List<CategoryImportTaskContext> taskContexts;

    private ImportJobStatus status;

    public ImportJob(final UUID id, final ImportJobStatus status) {
        guard(notNull(id));
        this.id = id;

        guard(notNull(status));
        this.status = status;

        this.taskContexts = new ArrayList<>();
    }

    public UUID getId() {
        return this.id;
    }

    public void setStatus(final ImportJobStatus status) {
        guard(notNull(status));
        this.status = status;
    }

    public ImportJobStatus getStatus() {
        return this.status;
    }

    public ImportTask getCurrentTask() {
        return null;
    }

}
