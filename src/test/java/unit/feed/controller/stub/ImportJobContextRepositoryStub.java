package unit.feed.controller.stub;

import nmd.orb.repositories.ImportJobContextRepository;
import nmd.orb.services.importer.ImportJobContext;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * @author : igu
 */
public class ImportJobContextRepositoryStub implements ImportJobContextRepository {

    private ImportJobContext job;

    @Override
    public ImportJobContext load() {
        return this.job;
    }

    @Override
    public void store(final ImportJobContext job) {
        guard(notNull(job));
        this.job = job;
    }

    @Override
    public void clear() {
        this.job = null;
    }

}
