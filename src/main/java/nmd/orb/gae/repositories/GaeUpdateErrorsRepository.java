package nmd.orb.gae.repositories;

import nmd.orb.repositories.UpdateErrorsRepository;
import nmd.orb.services.update.UpdateErrors;

import java.util.UUID;

/**
 * @author : igu
 */
public enum GaeUpdateErrorsRepository implements UpdateErrorsRepository {

    INSTANCE;

    @Override
    public void store(final UpdateErrors updateErrors) {

    }

    @Override
    public UpdateErrors load(final UUID feedId) {
        return null;
    }

    @Override
    public void delete(final UUID feedId) {

    }

}
