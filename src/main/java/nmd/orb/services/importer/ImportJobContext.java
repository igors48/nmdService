package nmd.orb.services.importer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * @author : igu
 */
public class ImportJobContext {

    private final UUID id; // TODO remove
    private final List<CategoryImportContext> contexts;

    private ImportJobStatus status;

    public ImportJobContext(final UUID id, final ImportJobStatus status) {
        guard(notNull(id));
        this.id = id;

        guard(notNull(status));
        this.status = status;

        this.contexts = new ArrayList<>();
    }

    //TODO tests
    public boolean canBeExecuted() {

        return this.status.equals(ImportJobStatus.STARTED) && (findExecutableContext() != null);

    }

    //TODO tests
    public CategoryImportContext findExecutableContext() {

        for (final CategoryImportContext context : this.contexts) {

            if (context.canBeExecuted()) {
                return context;
            }
        }

        return null;
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

    //step execution result - completed / not completed
}
