package unit.feed.controller.stub;

import nmd.orb.repositories.UpdateErrorsRepository;
import nmd.orb.services.update.UpdateErrors;

import java.util.UUID;

/**
 * @author : igu
 */
public class UpdateErrorsRepositoryStub implements UpdateErrorsRepository {

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

    public boolean isEmpty() {
        return true;
    }

}
