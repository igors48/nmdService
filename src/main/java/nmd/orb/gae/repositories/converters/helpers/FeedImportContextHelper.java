package nmd.orb.gae.repositories.converters.helpers;

import nmd.orb.services.importer.FeedImportContext;
import nmd.orb.services.importer.FeedImportTaskStatus;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * @author : igu
 */
public class FeedImportContextHelper {

    private String feedLink;
    private String feedTitle;
    private int triesLeft;
    private String status;

    public static FeedImportContextHelper convert(final FeedImportContext context) {
        guard(notNull(context));

        final FeedImportContextHelper helper = new FeedImportContextHelper();

        helper.feedLink = context.getFeedLink();
        helper.feedTitle = context.getFeedTitle();
        helper.triesLeft = context.getTriesLeft();
        helper.status = context.getStatus().toString();

        return helper;
    }

    public static FeedImportContext convert(final FeedImportContextHelper helper) {
        guard(notNull(helper));

        final String feedLink = helper.feedLink;
        final String feedTitle = helper.feedTitle;
        final int triesLeft = helper.triesLeft;
        final FeedImportTaskStatus status = FeedImportTaskStatus.valueOf(helper.status);

        return new FeedImportContext(feedLink, feedTitle, triesLeft, status);
    }

}
