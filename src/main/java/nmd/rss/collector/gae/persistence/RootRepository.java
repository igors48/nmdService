package nmd.rss.collector.gae.persistence;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

import java.util.UUID;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * User: igu
 * Date: 15.10.13
 */
public class RootRepository {

    public static final MemcacheService MEMCACHE_SERVICE = MemcacheServiceFactory.getMemcacheService();
    public static final DatastoreService DATASTORE_SERVICE = DatastoreServiceFactory.getDatastoreService();

    private static final String FEEDS_ENTITY_KIND = "Feeds";
    private static final String FEED_ENTITY_KIND = "Feed";

    private static final String FEED_ID = "feedId";

    public static Key getFeedRootKey(UUID feedId) {
        assertNotNull(feedId);
        /*
        final Key feedsRootKey = getFeedsRootKey();

        final Query query = new Query(FEED_ENTITY_KIND)
                .setAncestor(feedsRootKey)
                .setFilter(new Query.FilterPredicate(FEED_ID, EQUAL, feedId.toString()));
        final PreparedQuery preparedQuery = DATASTORE_SERVICE.prepare(query);

        return preparedQuery.asSingleEntity();
        */
        //return (Entity) MEMCACHE_SERVICE.get(FEED_ENTITY_KIND);

        return KeyFactory.createKey(getFeedsRootKey(), FEED_ENTITY_KIND, feedId.toString());
    }

    /*
    public static Entity createFeedRoot(UUID feedId) {
        assertNotNull(feedId);

        final Key feedsRootKey = getFeedsRootKey();

        final Entity feedRoot = newFeedRoot(feedId, feedsRootKey);
        DATASTORE_SERVICE.put(feedRoot);

        MEMCACHE_SERVICE.put(FEED_ENTITY_KIND, feedRoot);

        return feedRoot;
    }
    */
    private static Key getFeedsRootKey() {
        /*
        final Key rootKey = (Key) MEMCACHE_SERVICE.get(FEEDS_ENTITY_KIND);

        if (rootKey != null) {
            return rootKey;
        }

        final Query query = new Query(FEEDS_ENTITY_KIND);
        final PreparedQuery preparedQuery = DATASTORE_SERVICE.prepare(query);

        Entity feedsRoot = preparedQuery.asSingleEntity();

        if (feedsRoot == null) {
            feedsRoot = newFeedsRoot();

            DATASTORE_SERVICE.put(feedsRoot);
        }

        final Key newFeedsRootKey = feedsRoot.getKey();
        MEMCACHE_SERVICE.put(FEEDS_ENTITY_KIND, newFeedsRootKey);

        return newFeedsRootKey;
        */

        return KeyFactory.createKey(FEEDS_ENTITY_KIND, FEEDS_ENTITY_KIND);
    }

    private static Entity newFeedsRoot() {
        return new Entity(FEEDS_ENTITY_KIND);
    }

    private static Entity newFeedRoot(final UUID feedId, Key feedsKey) {
        assertNotNull(feedId);
        assertNotNull(feedsKey);

        final Entity entity = new Entity(FEED_ENTITY_KIND, feedsKey);

        entity.setProperty(FEED_ID, feedId.toString());

        return entity;
    }

}
