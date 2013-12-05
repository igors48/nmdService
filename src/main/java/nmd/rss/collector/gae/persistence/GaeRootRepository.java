package nmd.rss.collector.gae.persistence;

import com.google.appengine.api.datastore.*;
import nmd.rss.collector.Transactions;

import java.util.List;
import java.util.UUID;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import static java.lang.Integer.MAX_VALUE;
import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.collector.util.Assert.assertStringIsValid;

/**
 * User: igu
 * Date: 15.10.13
 */
public class GaeRootRepository implements Transactions {

    public static final DatastoreService DATASTORE_SERVICE = DatastoreServiceFactory.getDatastoreService();

    private static final String FEEDS_ENTITY_KIND = "Feeds";
    private static final String FEED_ENTITY_KIND = "Feed";

    private static final Key FEEDS_ROOT_KEY = KeyFactory.createKey(FEEDS_ENTITY_KIND, FEEDS_ENTITY_KIND);

    @Override
    public Transaction beginOne() {
        return DATASTORE_SERVICE.beginTransaction();
    }

    public static Key getFeedRootKey(final UUID feedId) {
        assertNotNull(feedId);

        return KeyFactory.createKey(FEEDS_ROOT_KEY, FEED_ENTITY_KIND, feedId.toString());
    }

    public static Entity loadEntity(final UUID feedId, final String kind, final boolean keysOnly) {
        assertNotNull(feedId);
        assertStringIsValid(kind);

        final PreparedQuery preparedQuery = prepareQuery(feedId, kind, keysOnly);

        return preparedQuery.asSingleEntity();
    }

    public static List<Entity> loadEntities(final String kind) {
        assertStringIsValid(kind);

        final Query query = new Query(kind);
        final PreparedQuery preparedQuery = DATASTORE_SERVICE.prepare(query);

        return preparedQuery.asList(withLimit(MAX_VALUE));
    }

    public static void deleteEntity(final UUID feedId, final String kind) {
        assertNotNull(feedId);
        assertStringIsValid(kind);

        final Entity victim = loadEntity(feedId, kind, true);

        if (victim != null) {
            DATASTORE_SERVICE.delete(victim.getKey());
        }
    }

    private static PreparedQuery prepareQuery(final UUID feedId, final String kind, final boolean keysOnly) {
        final Key feedRootKey = getFeedRootKey(feedId);
        final Query query = new Query(kind).setAncestor(feedRootKey);

        if (keysOnly) {
            query.setKeysOnly();
        }

        return DATASTORE_SERVICE.prepare(query);
    }

}
