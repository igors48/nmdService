package nmd.orb.gae.repositories.converters.helpers;

import nmd.orb.services.importer.FeedImportContext;
import nmd.orb.services.importer.FeedImportTaskStatus;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * @author : igu
 */
public class FeedImportContextHelper {

    private String link;
    private String title;
    private int tries;
    private String status;

    public static FeedImportContextHelper convert(final FeedImportContext context) {
        guard(notNull(context));

        final FeedImportContextHelper helper = new FeedImportContextHelper();

        helper.link = context.getFeedLink();
        helper.title = context.getFeedTitle();
        helper.tries = context.getTriesLeft();
        helper.status = context.getStatus().toString();

        return helper;
    }

    public static FeedImportContext convert(final FeedImportContextHelper helper) {
        guard(notNull(helper));

        final String feedLink = helper.link;
        final String feedTitle = helper.title;
        final int triesLeft = helper.tries;
        final FeedImportTaskStatus status = FeedImportTaskStatus.valueOf(helper.status);

        return new FeedImportContext(feedLink, feedTitle, triesLeft, status);
    }

}
