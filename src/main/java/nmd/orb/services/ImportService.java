package nmd.orb.services;

import com.google.appengine.api.datastore.Transaction;
import nmd.orb.error.ServiceException;
import nmd.orb.repositories.FeedImportJobRepository;
import nmd.orb.repositories.Transactions;
import nmd.orb.services.importer.FeedImportJob;
import nmd.orb.services.importer.FeedImportJobStatus;
import nmd.orb.services.report.FeedImportStatusReport;

import static nmd.orb.error.ServiceError.importJobStartedAlready;
import static nmd.orb.services.importer.FeedImportJobStatus.STARTED;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;
import static nmd.orb.util.TransactionTools.rollbackIfActive;

/**
 * Created by igor on 27.11.2014.
 */
public class ImportService {

    private final FeedImportJobRepository feedImportJobRepository;
    private final Transactions transactions;

    public ImportService(final FeedImportJobRepository feedImportJobRepository, final Transactions transactions) {
        guard(notNull(feedImportJobRepository));
        this.feedImportJobRepository = feedImportJobRepository;

        guard(notNull(transactions));
        this.transactions = transactions;
    }

    public void schedule(final FeedImportJob job) throws ServiceException {
        guard(notNull(job));

        Transaction transaction = null;

        try {
            transaction = this.transactions.beginOne();

            final FeedImportJob current = this.feedImportJobRepository.load();

            final boolean canNotBeScheduled = (current != null) && (current.getStatus().equals(STARTED));

            if (canNotBeScheduled) {
                throw new ServiceException(importJobStartedAlready());
            }

            this.feedImportJobRepository.store(job);

            transaction.commit();
        } finally {
            rollbackIfActive(transaction);
        }
    }

    public void executeOneImport() {

    }

    public void start() {
        changeStatus(FeedImportJobStatus.STARTED);
    }

    public void stop() {
        changeStatus(FeedImportJobStatus.STOPPED);
    }

    public void reject() {
        Transaction transaction = null;

        try {
            transaction = this.transactions.beginOne();

            this.feedImportJobRepository.clear();

            transaction.commit();
        } finally {
            rollbackIfActive(transaction);
        }
    }

    public FeedImportStatusReport status() {
        return null;
    }

    private void changeStatus(final FeedImportJobStatus status) {
        Transaction transaction = null;

        try {
            transaction = this.transactions.beginOne();

            final FeedImportJob current = this.feedImportJobRepository.load();

            if (current != null) {
                current.setStatus(status);
                this.feedImportJobRepository.store(current);
            }

            transaction.commit();
        } finally {
            rollbackIfActive(transaction);
        }
    }

}
