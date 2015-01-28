package nmd.orb.gae.repositories;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import nmd.orb.gae.repositories.datastore.Datastore;
import nmd.orb.gae.repositories.datastore.Kind;
import nmd.orb.repositories.ChangeRepository;
import nmd.orb.services.export.Change;

import java.util.List;

import static nmd.orb.gae.repositories.converters.ChangeConverter.convert;
import static nmd.orb.gae.repositories.datastore.GaeDatastoreTools.getEntityRootKey;
import static nmd.orb.gae.repositories.datastore.GaeDatastoreTools.loadEntities;
import static nmd.orb.gae.repositories.datastore.RootKind.CHANGE;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Created by igor on 18.01.2015.
 */
public enum GaeChangeRepository implements ChangeRepository {

    INSTANCE;

    private static final Key KEY = getEntityRootKey(CHANGE.toString(), CHANGE);

    @Override
    public Change load() {
        final List<Entity> entities = loadEntities(Kind.CHANGE);

        return entities.isEmpty() ? null : convert(entities.get(0));
    }

    @Override
    public void store(final Change change) {
        guard(notNull(change));

        clear();

        Datastore.INSTANCE.getDatastoreService().put(convert(change, KEY));
    }

    @Override
    public void clear() {
        final List<Entity> victims = loadEntities(Kind.CHANGE);

        for (final Entity victim : victims) {
            Datastore.INSTANCE.getDatastoreService().delete(victim.getKey());
        }

    }

}
