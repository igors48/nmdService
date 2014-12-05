package nmd.orb.services;

import com.google.appengine.api.datastore.Transaction;
import nmd.orb.error.ServiceException;
import nmd.orb.repositories.ImportJobContextRepository;
import nmd.orb.repositories.Transactions;
import nmd.orb.services.importer.*;
import nmd.orb.services.report.FeedImportStatusReport;

import static nmd.orb.error.ServiceError.importJobStartedAlready;
import static nmd.orb.services.importer.ImportJobStatus.STARTED;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;
import static nmd.orb.util.TransactionTools.rollbackIfActive;

/**
 * Created by igor on 27.11.2014.
 */
public class ImportService {

    private final ImportJobContextRepository importJobContextRepository;
    private final CategoriesServiceAdapter categoriesAdapter;
    private final FeedsServiceAdapter feedsAdapter;
    private final Transactions transactions;

    public ImportService(final ImportJobContextRepository importJobContextRepository, final CategoriesServiceAdapter categoriesAdapter, final FeedsServiceAdapter feedsAdapter, final Transactions transactions) {
        guard(notNull(importJobContextRepository));
        this.importJobContextRepository = importJobContextRepository;

        guard(notNull(categoriesAdapter));
        this.categoriesAdapter = categoriesAdapter;

        guard(notNull(feedsAdapter));
        this.feedsAdapter = feedsAdapter;

        guard(notNull(transactions));
        this.transactions = transactions;
    }

    public void schedule(final ImportJobContext job) throws ServiceException {
        guard(notNull(job));

        Transaction transaction = null;

        try {
            transaction = this.transactions.beginOne();

            final ImportJobContext current = this.importJobContextRepository.load();

            final boolean canNotBeScheduled = (current != null) && (current.getStatus().equals(STARTED));

            if (canNotBeScheduled) {
                throw new ServiceException(importJobStartedAlready());
            }

            this.importJobContextRepository.store(job);

            transaction.commit();
        } finally {
            rollbackIfActive(transaction);
        }
    }

    public void executeOneImport() {
        Transaction transaction = null;

        try {
            transaction = this.transactions.beginOne();

            final ImportJobContext context = this.importJobContextRepository.load();

            if (context.canBeExecuted()) {
                ImportJob.execute(context, this.categoriesAdapter, this.feedsAdapter);
            }

            this.importJobContextRepository.store(context);

            transaction.commit();
        } finally {
            rollbackIfActive(transaction);
        }
    }

    public void start() {
        changeStatus(ImportJobStatus.STARTED);
    }

    public void stop() {
        changeStatus(ImportJobStatus.STOPPED);
    }

    public void reject() {
        Transaction transaction = null;

        try {
            transaction = this.transactions.beginOne();

            this.importJobContextRepository.clear();

            transaction.commit();
        } finally {
            rollbackIfActive(transaction);
        }
    }

    public FeedImportStatusReport status() {
        return null;
    }

    private void changeStatus(final ImportJobStatus status) {
        Transaction transaction = null;

        try {
            transaction = this.transactions.beginOne();

            final ImportJobContext current = this.importJobContextRepository.load();

            if (current != null) {
                current.setStatus(status);
                this.importJobContextRepository.store(current);
            }

            transaction.commit();
        } finally {
            rollbackIfActive(transaction);
        }
    }

}
