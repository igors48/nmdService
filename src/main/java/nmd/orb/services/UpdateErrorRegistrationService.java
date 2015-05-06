package nmd.orb.services;

import nmd.orb.repositories.UpdateErrorsRepository;

import java.util.UUID;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * @author : igu
 */
public class UpdateErrorRegistrationService {

    private final UpdateErrorsRepository updateErrorsRepository;

    public UpdateErrorRegistrationService(final UpdateErrorsRepository updateErrorsRepository) {
        guard(notNull(updateErrorsRepository));
        this.updateErrorsRepository = updateErrorsRepository;
    }

    public void updateError(final UUID feedId) {

    }

    public void updateSuccess(final UUID feedId) {

    }

    public void delete(final UUID feedId) {

    }

    public int getErrorCount(final UUID feedId) {
        return 0;
    }

}

