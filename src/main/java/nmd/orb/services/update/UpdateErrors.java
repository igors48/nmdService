package nmd.orb.services.update;

import nmd.orb.util.Parameter;

import java.io.Serializable;
import java.util.UUID;

import static nmd.orb.feed.FeedHeader.isValidFeedHeaderId;
import static nmd.orb.util.Assert.guard;

/**
 * @author : igu
 */
public class UpdateErrors implements Serializable {

    public final UUID feedId;
    public final int errorsCount;

    public UpdateErrors(final UUID feedId) {
        this(feedId, 0);
    }

    public UpdateErrors(final UUID feedId, final int errorsCount) {
        guard(isValidFeedHeaderId(this.feedId = feedId));
        guard(isValidErrorsCount(this.errorsCount = errorsCount));
    }

    public UpdateErrors incErrors() {
        return new UpdateErrors(this.feedId, this.errorsCount + 1);
    }

    public static boolean isValidErrorsCount(final int count) {
        return Parameter.isPositive(count);
    }

}
