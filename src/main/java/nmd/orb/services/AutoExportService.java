package nmd.orb.services;

import nmd.orb.repositories.ChangeRepository;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Created by igor on 18.01.2015.
 */
public class AutoExportService {

    private final ChangeRepository changeRepository;

    public AutoExportService(final ChangeRepository changeRepository) {
        guard(notNull(changeRepository));
        this.changeRepository = changeRepository;
    }

    public void registerChange() {

    }

    public void export() {

    }

}
