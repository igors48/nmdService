package unit.feed.controller.stub;

import nmd.orb.repositories.UpdateErrorsRepository;
import nmd.orb.services.update.UpdateErrors;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author : igu
 */
public class UpdateErrorsRepositoryStub implements UpdateErrorsRepository {

    private final Map<UUID, UpdateErrors> errorsMap;

    public UpdateErrorsRepositoryStub() {
        this.errorsMap = new HashMap<>();
    }

    @Override
    public void store(final UpdateErrors updateErrors) {
        this.errorsMap.put(updateErrors.feedId, updateErrors);
    }

    @Override
    public UpdateErrors load(final UUID feedId) {
        return this.errorsMap.get(feedId);
    }

    @Override
    public void delete(final UUID feedId) {
        this.errorsMap.remove(feedId);
    }

    public boolean isEmpty() {
        return this.errorsMap.isEmpty();
    }

}
