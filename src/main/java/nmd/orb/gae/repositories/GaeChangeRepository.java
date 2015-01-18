package nmd.orb.gae.repositories;

import nmd.orb.repositories.ChangeRepository;
import nmd.orb.services.export.Change;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Created by igor on 18.01.2015.
 */
public class GaeChangeRepository implements ChangeRepository {

    public static final ChangeRepository GAE_CHANGE_REPOSITORY = new GaeChangeRepository();

    @Override
    public Change load() {
        return null;
    }

    @Override
    public void store(final Change context) {
        guard(notNull(context));

    }

    @Override
    public void clear() {

    }

}
