package nmd.orb.services.importer;

import static nmd.orb.feed.FeedHeader.isValidFeedHeaderTitle;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.*;

/**
 * @author : igu
 */
public class FeedImportContext {

    private final String feedLink;
    private final String feedTitle;

    private int triesLeft;
    private FeedImportTaskStatus status;

    public FeedImportContext(final String feedLink, final String feedTitle, final int triesLeft, final FeedImportTaskStatus status) {
        guard(isValidUrl(feedLink));
        this.feedLink = feedLink;

        guard(isValidFeedHeaderTitle(feedTitle));
        this.feedTitle = feedTitle;

        guard(isPositive(triesLeft));
        this.triesLeft = triesLeft;

        guard(notNull(status));
        this.status = status;
    }

    public boolean canBeExecuted() {

        return this.triesLeft >= 1 && (this.status.equals(FeedImportTaskStatus.WAITING) || this.status.equals(FeedImportTaskStatus.ERROR));

    }

    public FeedImportTaskStatus getStatus() {
        return this.status;
    }

    public String getFeedLink() {
        return this.feedLink;
    }

    public String getFeedTitle() {
        return this.feedTitle;
    }

    public void decreaseTries() {
        this.triesLeft = this.triesLeft == 0 ? 0 : --this.triesLeft;
    }

    public void setStatus(final FeedImportTaskStatus status) {
        guard(notNull(status));
        this.status = status;
    }

    public int getTriesLeft() {
        return this.triesLeft;
    }
}
