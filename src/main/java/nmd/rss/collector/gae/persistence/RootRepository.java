package nmd.rss.collector.gae.persistence;

import com.google.appengine.api.datastore.*;

import java.util.UUID;

import static com.google.appengine.api.datastore.Query.FilterOperator.EQUAL;
import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * User: igu
 * Date: 15.10.13
 */
public class RootRepository {

    public static final DatastoreService DATASTORE_SERVICE = DatastoreServiceFactory.getDatastoreService();

    private static final String FEEDS_ENTITY_KIND = "Feeds";
    private static final String FEED_ENTITY_KIND = "Feed";

    private static final String FEED_ID = "feedId";

    public static Entity getFeedRoot(UUID feedId) {
        assertNotNull(feedId);

        final Entity root = getFeedsRoot();

        final Query query = new Query(FEED_ENTITY_KIND)
                .setAncestor(root.getKey())
                .setFilter(new Query.FilterPredicate(FEED_ID, EQUAL, feedId.toString()));
        final PreparedQuery preparedQuery = DATASTORE_SERVICE.prepare(query);

        return preparedQuery.asSingleEntity();
    }

    public static Entity createFeedRoot(UUID feedId) {
        assertNotNull(feedId);

        final Entity feedsRoot = getFeedsRoot();

        final Entity feedRoot = newFeedRoot(feedId, feedsRoot.getKey());
        DATASTORE_SERVICE.put(feedRoot);

        return feedRoot;
    }

    public static Key getFeedRootKey(UUID feedId) {
        assertNotNull(feedId);

        final Entity feedRoot = getFeedRoot(feedId);

        return feedRoot == null ? null : feedRoot.getKey();
    }

    private static Entity getFeedsRoot() {
        final Query query = new Query(FEEDS_ENTITY_KIND);
        final PreparedQuery preparedQuery = DATASTORE_SERVICE.prepare(query);

        Entity feedsRoot = preparedQuery.asSingleEntity();

        if (feedsRoot == null) {
            feedsRoot = newFeedsRoot();

            DATASTORE_SERVICE.put(feedsRoot);
        }

        return feedsRoot;
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
