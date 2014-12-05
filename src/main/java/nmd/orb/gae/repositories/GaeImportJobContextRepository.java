package nmd.orb.gae.repositories;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import nmd.orb.gae.repositories.datastore.Kind;
import nmd.orb.repositories.ImportJobContextRepository;
import nmd.orb.services.importer.ImportJobContext;

import java.util.List;

import static nmd.orb.gae.repositories.converters.ImportJobContextConverter.convert;
import static nmd.orb.gae.repositories.datastore.GaeDatastoreTools.*;
import static nmd.orb.gae.repositories.datastore.RootKind.IMPORT;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * @author : igu
 */
public class GaeImportJobContextRepository implements ImportJobContextRepository {

    public static final GaeImportJobContextRepository GAE_IMPORT_JOB_CONTEXT_REPOSITORY = new GaeImportJobContextRepository();

    private static final Key KEY = getEntityRootKey(IMPORT.toString(), IMPORT);

    @Override
    public ImportJobContext load() {
        final List<Entity> entities = loadEntities(Kind.IMPORT);

        return entities.isEmpty() ? null : convert(entities.get(0));
    }

    @Override
    public void store(final ImportJobContext context) {
        guard(notNull(context));

        clear();

        DATASTORE_SERVICE.put(convert(context, KEY));
    }

    @Override
    public void clear() {
        DATASTORE_SERVICE.delete(KEY);
    }

}
