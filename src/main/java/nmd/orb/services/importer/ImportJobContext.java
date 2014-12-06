package nmd.orb.services.importer;

import nmd.orb.error.ServiceException;
import nmd.orb.http.responses.payload.BackupReportPayload;

import java.util.ArrayList;
import java.util.List;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.isPositive;
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

    public boolean canBeExecuted() {

        return this.status.equals(ImportJobStatus.STARTED);

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

    public List<CategoryImportContext> getContexts() {
        return this.contexts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImportJobContext that = (ImportJobContext) o;

        if (!contexts.equals(that.contexts)) return false;
        if (status != that.status) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = contexts.hashCode();
        result = 31 * result + status.hashCode();
        return result;
    }

    public static ImportJobContext convert(final List<BackupReportPayload> payloads, final int triesCount) throws ServiceException {
        guard(notNull(payloads));
        guard(isPositive(triesCount));

        final List<CategoryImportContext> contexts = new ArrayList<>();

        for (final BackupReportPayload payload : payloads) {
            contexts.add(CategoryImportContext.convert(payload.category, payload.feeds, triesCount));
        }

        return new ImportJobContext(contexts, ImportJobStatus.STOPPED);
    }

}
