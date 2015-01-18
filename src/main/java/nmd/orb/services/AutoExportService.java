package nmd.orb.services;

import com.google.appengine.api.datastore.Transaction;
import nmd.orb.http.responses.ExportReportResponse;
import nmd.orb.repositories.ChangeRepository;
import nmd.orb.repositories.Transactions;
import nmd.orb.services.export.Change;
import nmd.orb.services.report.ExportReport;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;
import static nmd.orb.util.TransactionTools.rollbackIfActive;

/**
 * Created by igor on 18.01.2015.
 */
public class AutoExportService {

    public static final long FIVE_MINUTES = 5 * 60 * 1000;

    private final ChangeRepository changeRepository;
    private final CategoriesService categoriesService;
    private final MailService mailService;

    private final Transactions transactions;

    public AutoExportService(final ChangeRepository changeRepository, final CategoriesService categoriesService, final MailService mailService, final Transactions transactions) {
        guard(notNull(changeRepository));
        this.changeRepository = changeRepository;

        guard(notNull(categoriesService));
        this.categoriesService = categoriesService;

        guard(notNull(mailService));
        this.mailService = mailService;

        guard(notNull(transactions));
        this.transactions = transactions;
    }

    public boolean export(final long currentTime) {
        Transaction transaction = null;

        boolean notificationIsSent = false;

        try {
            transaction = this.transactions.beginOne();

            final Change change = this.changeRepository.load();

            if (!change.isNotificationIsSent()) {
                final long period = currentTime - change.getTimestamp();

                final boolean sendIsNeeded = (period > FIVE_MINUTES);

                if (sendIsNeeded) {
                    final ExportReport exportReport = this.categoriesService.createExportReport();
                    final ExportReportResponse attachment = ExportReportResponse.create(exportReport);

                    this.mailService.sendChangeNotification(attachment);

                    final Change marked = change.markAsSent();
                    this.changeRepository.store(marked);

                    notificationIsSent = true;
                }
            }

            transaction.commit();

            return notificationIsSent;
        } finally {
            rollbackIfActive(transaction);
        }
    }

}
