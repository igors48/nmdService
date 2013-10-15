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

    //TODO divide get and create
    public static Entity getFeedRoot(UUID feedId) {
        final Entity root = getFeedsRoot();

        final Query query = new Query(FEED_ENTITY_KIND)
                .setAncestor(root.getKey())
                .setFilter(new Query.FilterPredicate(FEED_ID, EQUAL, feedId.toString()));
        final PreparedQuery preparedQuery = DATASTORE_SERVICE.prepare(query);

        Entity feedRoot = preparedQuery.asSingleEntity();

        if (feedRoot == null) {
            feedRoot = newFeedRoot(feedId, root.getKey());
            DATASTORE_SERVICE.put(feedRoot);
        }

        return feedRoot;
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
