package unit.feed.controller.stub;

import nmd.orb.repositories.FeedImportJobRepository;
import nmd.orb.services.importer.FeedImportJob;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * @author : igu
 */
public class FeedImportJobRepositoryStub implements FeedImportJobRepository {

    private FeedImportJob job;

    @Override
    public FeedImportJob load() {
        return this.job;
    }

    @Override
    public void store(final FeedImportJob job) {
        guard(notNull(job));
        this.job = job;
    }

    @Override
    public void clear() {
        this.job = null;
    }

}
