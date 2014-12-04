package nmd.orb.services.importer;

import java.util.List;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * @author : igu
 */
public class ImportJobContext {

    private final List<CategoryImportContext> contexts;

    private ImportJobStatus status;

    public ImportJobContext(final List<CategoryImportContext> contexts, final ImportJobStatus status) {
        guard(notNull(contexts));
        this.contexts = contexts;

        guard(notNull(status));
        this.status = status;
    }

    //TODO tests
    public boolean canBeExecuted() {

        return this.status.equals(ImportJobStatus.STARTED) && (findExecutableContext() != null);

    }

    public CategoryImportContext findExecutableContext() {

        for (final CategoryImportContext context : this.contexts) {

            if (context.canBeExecuted()) {
                return context;
            }
        }

        return null;
    }

    public void setStatus(final ImportJobStatus status) {
        guard(notNull(status));
        this.status = status;
    }

    public ImportJobStatus getStatus() {
        return this.status;
    }

}
