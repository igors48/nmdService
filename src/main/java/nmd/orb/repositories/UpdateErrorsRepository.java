package nmd.orb.repositories;

import nmd.orb.services.update.UpdateErrors;

import java.util.UUID;

/**
 * @author : igu
 */
public interface UpdateErrorsRepository {

    void store(UpdateErrors updateErrors);

    UpdateErrors load(UUID feedId);

    void delete(UUID feedId);

}
