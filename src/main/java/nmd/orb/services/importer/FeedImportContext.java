package nmd.orb.services.importer;

import nmd.orb.error.ServiceError;
import nmd.orb.error.ServiceException;
import nmd.orb.feed.FeedHeader;
import nmd.orb.http.responses.payload.FeedHeaderPayload;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FeedImportContext that = (FeedImportContext) o;

        if (triesLeft != that.triesLeft) return false;
        if (!feedLink.equals(that.feedLink)) return false;
        if (!feedTitle.equals(that.feedTitle)) return false;
        if (status != that.status) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = feedLink.hashCode();
        result = 31 * result + feedTitle.hashCode();
        result = 31 * result + triesLeft;
        result = 31 * result + status.hashCode();
        return result;
    }

    public static FeedImportContext convert(final FeedHeaderPayload payload, final int triesCount) throws ServiceException {
        guard(notNull(payload));
        guard(isPositive(triesCount));

        if (!isValidUrl(payload.feedLink)) {
            throw new ServiceException(ServiceError.invalidFeedUrl(payload.feedLink));
        }

        if (!FeedHeader.isValidFeedHeaderTitle(payload.feedTitle)) {
            throw new ServiceException(ServiceError.invalidFeedTitle(payload.feedTitle));
        }

        return new FeedImportContext(payload.feedLink, payload.feedTitle, triesCount, FeedImportTaskStatus.WAITING);
    }

}
