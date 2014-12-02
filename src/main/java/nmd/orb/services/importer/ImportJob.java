package nmd.orb.services.importer;

import nmd.orb.error.ServiceException;

import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.format;
import static nmd.orb.reader.Category.isValidCategoryId;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Created by igor on 02.12.2014.
 */
public class ImportJob {

    private static final Logger LOGGER = Logger.getLogger(ImportJob.class.getName());

    public static void execute(final FeedImportContext context, final String categoryId, final FeedsServiceAdapter adapter) {
        guard(notNull(context));
        guard(isValidCategoryId(categoryId));
        guard(notNull(adapter));

        final String feedLink = context.getFeedLink();
        final String feedTitle = context.getFeedTitle();

        try {
            context.decreaseTries();

            adapter.addFeed(feedLink, feedTitle, categoryId);

            context.setStatus(FeedImportTaskStatus.COMPLETED);

            LOGGER.info(format("Feed [ %s ] with title [ %s ] added to category [ %s ]", feedLink, feedTitle, categoryId));
        } catch (ServiceException exception) {
            LOGGER.log(Level.SEVERE, format("Error adding feed [ %s ] with title [ %s ] to category [ %s ]", feedLink, feedTitle, categoryId), exception);

            final int triesLeft = context.getTriesLeft();
            final FeedImportTaskStatus status = triesLeft == 0 ? FeedImportTaskStatus.FAILED : FeedImportTaskStatus.ERROR;

            context.setStatus(status);

            LOGGER.info(format("Tries left [ %d ] new status is [ %s ]", triesLeft, status));
        }
    }
}
