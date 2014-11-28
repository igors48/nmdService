package nmd.orb.services;

import nmd.orb.error.ServiceException;
import nmd.orb.repositories.FeedImportJobRepository;
import nmd.orb.services.importer.FeedImportJob;
import nmd.orb.services.report.FeedImportStatusReport;

import static nmd.orb.error.ServiceError.importJobStartedAlready;
import static nmd.orb.services.importer.FeedImportJobStatus.STARTED;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Created by igor on 27.11.2014.
 */
public class ImportService {

    private final FeedImportJobRepository feedImportJobRepository;

    public ImportService(final FeedImportJobRepository feedImportJobRepository) {
        this.feedImportJobRepository = feedImportJobRepository;
    }

    public void schedule(final FeedImportJob job) throws ServiceException {
        guard(notNull(job));

        final FeedImportJob current = this.feedImportJobRepository.load();

        final boolean canNotBeScheduled = (current != null) && (current.status.equals(STARTED));

        if (canNotBeScheduled) {
            throw new ServiceException(importJobStartedAlready());
        }

        this.feedImportJobRepository.store(job);
    }

    public void executeOneImport() {

    }

    public FeedImportStatusReport start() {
        return null;
    }

    public FeedImportStatusReport stop() {
        return null;
    }

    public FeedImportStatusReport status() {
        return null;
    }

    public FeedImportStatusReport reject() {
        return null;
    }

}
