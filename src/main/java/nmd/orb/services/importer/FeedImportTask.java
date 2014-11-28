package nmd.orb.services.importer;

import static nmd.orb.feed.FeedHeader.isValidFeedHeaderTitle;
import static nmd.orb.reader.Category.isValidCategoryName;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.*;

/**
 * @author : igu
 */
public class FeedImportTask {

    private final String feedLink;
    private final String feedTitle;
    private final String categoryName;

    private int triesLeft;
    private FeedImportTaskStatus status;

    public FeedImportTask(final String feedLink, final String feedTitle, final String categoryName, final int triesLeft, final FeedImportTaskStatus status) {
        guard(isValidUrl(feedLink));
        this.feedLink = feedLink;

        guard(isValidFeedHeaderTitle(feedTitle));
        this.feedTitle = feedTitle;

        guard(isValidCategoryName(categoryName));
        this.categoryName = categoryName;

        guard(isPositive(triesLeft));
        this.triesLeft = triesLeft;

        guard(notNull(status));
        this.status = status;
    }

}
