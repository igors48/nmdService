package nmd.orb.services.importer;

import static nmd.orb.feed.FeedHeader.isValidFeedHeaderTitle;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.*;

/**
 * @author : igu
 */
public class FeedImportTaskContext {

    private final String feedLink;
    private final String feedTitle;

    private int triesLeft;
    private FeedImportTaskStatus status;

    public FeedImportTaskContext(final String feedLink, final String feedTitle, final int triesLeft, final FeedImportTaskStatus status) {
        guard(isValidUrl(feedLink));
        this.feedLink = feedLink;

        guard(isValidFeedHeaderTitle(feedTitle));
        this.feedTitle = feedTitle;

        guard(isPositive(triesLeft));
        this.triesLeft = triesLeft;

        guard(notNull(status));
        this.status = status;
    }

}
