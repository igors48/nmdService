package nmd.rss.collector.gae.persistence;

import com.google.appengine.api.datastore.*;
import nmd.rss.collector.Transactions;

import java.util.UUID;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * User: igu
 * Date: 15.10.13
 */
public class GaeRootRepository implements Transactions {

    public static final DatastoreService DATASTORE_SERVICE = DatastoreServiceFactory.getDatastoreService();

    private static final String FEEDS_ENTITY_KIND = "Feeds";
    private static final String FEED_ENTITY_KIND = "Feed";

    private static final Key FEEDS_ROOT_KEY = KeyFactory.createKey(FEEDS_ENTITY_KIND, FEEDS_ENTITY_KIND);

    public static Key getFeedRootKey(UUID feedId) {
        assertNotNull(feedId);

        return KeyFactory.createKey(FEEDS_ROOT_KEY, FEED_ENTITY_KIND, feedId.toString());
    }

    @Override
    public Transaction beginOne() {
        return DATASTORE_SERVICE.beginTransaction();
    }

}
