package nmd.orb.gae.repositories;

import nmd.orb.repositories.ImportJobContextRepository;
import nmd.orb.services.importer.ImportJobContext;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * @author : igu
 */
public class GaeImportJobContextRepository implements ImportJobContextRepository {

    @Override
    public ImportJobContext load() {
        return null;
    }

    @Override
    public void store(final ImportJobContext context) {
        guard(notNull(context));

        clear();
    }

    @Override
    public void clear() {
    }

}
