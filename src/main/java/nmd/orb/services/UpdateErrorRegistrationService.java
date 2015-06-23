package nmd.orb.services;

import nmd.orb.error.ServiceError;
import nmd.orb.repositories.UpdateErrorsRepository;
import nmd.orb.services.update.UpdateErrors;

import java.util.UUID;

import static nmd.orb.feed.FeedHeader.isValidFeedHeaderId;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * @author : igu
 */
public class UpdateErrorRegistrationService {

    public static final int MAX_SEQUENTIAL_UPDATE_ERRORS_COUNT = 2;
    public static final int MAX_STORED_ERRORS_COUNT = MAX_SEQUENTIAL_UPDATE_ERRORS_COUNT * 2;

    private final UpdateErrorsRepository updateErrorsRepository;

    public UpdateErrorRegistrationService(final UpdateErrorsRepository updateErrorsRepository) {
        guard(notNull(updateErrorsRepository));
        this.updateErrorsRepository = updateErrorsRepository;
    }

    public void updateError(final UUID feedId, final ServiceError error) {
        guard(isValidFeedHeaderId(feedId));
        guard(notNull(error));

        final UpdateErrors updateErrors = load(feedId).appendError(error);

        this.updateErrorsRepository.store(updateErrors);
    }

    public void updateSuccess(final UUID feedId) {
        guard(isValidFeedHeaderId(feedId));

        this.updateErrorsRepository.delete(feedId);
    }

    public void delete(final UUID feedId) {
        guard(isValidFeedHeaderId(feedId));

        this.updateErrorsRepository.delete(feedId);
    }

    public int getErrorCount(final UUID feedId) {
        guard(isValidFeedHeaderId(feedId));

        final UpdateErrors updateErrors = load(feedId);

        return updateErrors.errors.size();
    }

    public UpdateErrors load(final UUID feedId) {
        guard(isValidFeedHeaderId(feedId));

        final UpdateErrors updateErrors = this.updateErrorsRepository.load(feedId);

        return updateErrors == null ? new UpdateErrors(feedId, MAX_STORED_ERRORS_COUNT) : updateErrors;
    }

}

