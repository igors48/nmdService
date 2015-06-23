package nmd.orb.services.update;

import nmd.orb.error.ServiceError;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static nmd.orb.feed.FeedHeader.isValidFeedHeaderId;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * @author : igu
 */
public class UpdateErrors implements Serializable {

    public final UUID feedId;
    public final List<ServiceError> errors;

    public UpdateErrors(final UUID feedId) {
        this(feedId, new ArrayList<ServiceError>());
    }

    public UpdateErrors(final UUID feedId, final List<ServiceError> errors) {
        guard(isValidFeedHeaderId(this.feedId = feedId));
        guard(notNull(this.errors = errors));
    }

    public UpdateErrors appendError(final ServiceError error) {
        guard(notNull(error));

        final List<ServiceError> updatedErrors = new ArrayList<>(this.errors);
        updatedErrors.add(error);

        return new UpdateErrors(this.feedId, updatedErrors);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UpdateErrors that = (UpdateErrors) o;

        if (!feedId.equals(that.feedId)) return false;
        return errors.equals(that.errors);

    }

    @Override
    public int hashCode() {
        int result = feedId.hashCode();
        result = 31 * result + errors.hashCode();
        return result;
    }

}
