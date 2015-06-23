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

    private final int maxStoredErrorsCount;

    public UpdateErrors(final UUID feedId, final int maxStoredErrorsCount) {
        this(feedId, maxStoredErrorsCount, new ArrayList<ServiceError>());
    }

    private UpdateErrors(final UUID feedId, final int maxStoredErrorsCount, final List<ServiceError> errors) {
        guard(isValidFeedHeaderId(this.feedId = feedId));
        guard(isValidMaxStoredErrorsCount(this.maxStoredErrorsCount = maxStoredErrorsCount));
        guard(notNull(this.errors = errors));
    }

    public UpdateErrors appendError(final ServiceError error) {
        guard(notNull(error));

        List<ServiceError> updatedErrors = new ArrayList<>(this.errors);
        updatedErrors.add(error);

        updatedErrors = tail(updatedErrors, this.maxStoredErrorsCount);

        return new UpdateErrors(this.feedId, this.maxStoredErrorsCount, updatedErrors);
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

    public static boolean isValidMaxStoredErrorsCount(final int value) {
        return value >= 2;
    }

    public static List<ServiceError> tail(final List<ServiceError> list, final int size) {
        guard(notNull(list));
        guard(size > 1);

        final int listSize = list.size();

        if (listSize > size) {
            final int fromIndex = listSize - size;

            return list.subList(fromIndex, listSize);
        }

        return list;
    }

}
