package nmd.orb.services;

import com.google.appengine.api.datastore.Transaction;
import nmd.orb.error.ServiceError;
import nmd.orb.error.ServiceException;
import nmd.orb.http.responses.ExportReportResponse;
import nmd.orb.repositories.ChangeRepository;
import nmd.orb.repositories.Transactions;
import nmd.orb.services.export.Change;
import nmd.orb.services.report.ExportReport;

import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.format;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;
import static nmd.orb.util.TransactionTools.rollbackIfActive;

/**
 * Created by igor on 18.01.2015.
 */
public class AutoExportService {

    private static final Logger LOGGER = Logger.getLogger(AutoExportService.class.getName());

    public static final long TWO_MINUTES = 2 * 60 * 1000;

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

            if (change != null && !change.isNotificationIsSent()) {
                final long period = currentTime - change.getTimestamp();

                final boolean sendIsNeeded = (period > TWO_MINUTES);

                if (sendIsNeeded) {
                    final ExportReport exportReport = this.categoriesService.buildExportReport();
                    final ExportReportResponse attachment = ExportReportResponse.create(exportReport);

                    this.mailService.sendChangeNotification(attachment);

                    final Change marked = change.markAsSent();
                    this.changeRepository.store(marked);

                    notificationIsSent = true;

                    LOGGER.info("Mail with exported feeds was sent");
                }
            }

            transaction.commit();

            return notificationIsSent;
        } catch (ServiceException exception) {
            final ServiceError serviceError = exception.getError();
            LOGGER.log(Level.SEVERE, format("Error while auto export [ %s ]", serviceError), exception);

            return false;
        } finally {
            rollbackIfActive(transaction);
        }
    }

}
