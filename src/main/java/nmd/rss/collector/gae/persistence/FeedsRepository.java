package nmd.rss.collector.gae.persistence;

import com.google.appengine.api.datastore.*;

import java.util.UUID;

/**
 * User: igu
 * Date: 15.10.13
 */
public class FeedsRepository {

    private static final DatastoreService DATASTORE_SERVICE = DatastoreServiceFactory.getDatastoreService();

    public Entity getFeedsRoot() {
        final Query query = new Query(Entities.FEEDS_ENTITY_KIND);
        final PreparedQuery preparedQuery = DATASTORE_SERVICE.prepare(query);

        Entity feedsRoot = preparedQuery.asSingleEntity();

        if (feedsRoot == null) {
            feedsRoot = Entities.feeds();
            DATASTORE_SERVICE.put(feedsRoot);
        }

        return feedsRoot;
    }

    public Entity getFeedRoot(UUID feedId) {
        return null;
    }

}
