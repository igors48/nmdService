package unit.feed.controller.stub;

import nmd.orb.repositories.ImportJobContextRepository;
import nmd.orb.services.importer.ImportJobContext;

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
    public void store(final ImportJobContext context) {
        this.job = context;
    }

    @Override
    public void clear() {
        this.job = null;
    }

    public boolean isEmpty() {
        return this.job == null;
    }

}
