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

    public UpdateErrors(final UUID feedId, final int errorsCount) {
        guard(isValidFeedHeaderId(this.feedId = feedId));
        guard(isValidErrorsCount(this.errorsCount = errorsCount));
    }

    public static boolean isValidErrorsCount(final int count) {
        return Parameter.isPositive(count);
    }

}
