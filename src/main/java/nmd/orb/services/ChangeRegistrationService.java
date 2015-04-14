package nmd.orb.services;

import nmd.orb.repositories.ChangeRepository;
import nmd.orb.services.export.Change;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Created by igor on 18.01.2015.
 */
public class ChangeRegistrationService {

    private final ChangeRepository changeRepository;

    public ChangeRegistrationService(final ChangeRepository changeRepository) {
        guard(notNull(changeRepository));
        this.changeRepository = changeRepository;
    }

    public void registerChange() {
        final long timestamp = System.currentTimeMillis();
        final Change change = new Change(timestamp);

        this.changeRepository.store(change);
    }

}
