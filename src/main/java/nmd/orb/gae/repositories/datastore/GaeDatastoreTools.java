package nmd.orb.gae.repositories.datastore;

import com.google.appengine.api.datastore.*;

import java.util.List;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import static java.lang.Integer.MAX_VALUE;
import static nmd.orb.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date: 15.10.13
 */
public class GaeDatastoreTools {

    public static final DatastoreService DATASTORE_SERVICE = DatastoreServiceFactory.getDatastoreService();

    private static final String FEEDS_ENTITY_KIND = "Feeds";

    private static final Key ROOT_KEY = KeyFactory.createKey(FEEDS_ENTITY_KIND, FEEDS_ENTITY_KIND);

    public static Key getEntityRootKey(final String uuid, final RootKind rootKind) {
        assertNotNull(uuid);
        assertNotNull(rootKind);

        return KeyFactory.createKey(ROOT_KEY, rootKind.value, uuid);
    }

    public static Entity loadEntity(final String uuid, final RootKind rootKind, final Kind kind, final boolean keysOnly) {
        assertNotNull(uuid);
        assertNotNull(kind);

        final PreparedQuery preparedQuery = prepareQuery(uuid, rootKind, kind, keysOnly);

        return preparedQuery.asSingleEntity();
    }

    public static List<Entity> loadEntities(final Kind kind) {
        assertNotNull(kind);

        final Query query = new Query(kind.value);
        final PreparedQuery preparedQuery = DATASTORE_SERVICE.prepare(query);

        return preparedQuery.asList(withLimit(MAX_VALUE));
    }

    public static void deleteEntity(final String uuid, final RootKind rootKind, final Kind kind) {
        assertNotNull(uuid);
        assertNotNull(kind);

        final Entity victim = loadEntity(uuid, rootKind, kind, true);

        if (victim != null) {
            DATASTORE_SERVICE.delete(victim.getKey());
        }
    }

    private static PreparedQuery prepareQuery(final String uuid, final RootKind rootKind, final Kind kind, final boolean keysOnly) {
        final Key feedRootKey = getEntityRootKey(uuid, rootKind);
        final Query query = new Query(kind.value).setAncestor(feedRootKey);

        if (keysOnly) {
            query.setKeysOnly();
        }

        return DATASTORE_SERVICE.prepare(query);
    }

    private GaeDatastoreTools() {
        // empty
    }

}
