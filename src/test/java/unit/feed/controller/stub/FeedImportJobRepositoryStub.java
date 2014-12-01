package unit.feed.controller.stub;

import nmd.orb.repositories.FeedImportJobRepository;
import nmd.orb.services.importer.ImportJob;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * @author : igu
 */
public class FeedImportJobRepositoryStub implements FeedImportJobRepository {

    private ImportJob job;

    @Override
    public ImportJob load() {
        return this.job;
    }

    @Override
    public void store(final ImportJob job) {
        guard(notNull(job));
        this.job = job;
    }

    @Override
    public void clear() {
        this.job = null;
    }

}
