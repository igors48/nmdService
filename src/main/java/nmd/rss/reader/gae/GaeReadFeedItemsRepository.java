package nmd.rss.reader.gae;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import nmd.rss.reader.ReadFeedItemsRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static nmd.rss.collector.gae.persistence.GaeRootRepository.*;
import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.reader.gae.ReadFeedIdSetConverter.KIND;
import static nmd.rss.reader.gae.ReadFeedIdSetConverter.convert;

/**
 * User: igu
 * Date: 26.11.13
 */
public class GaeReadFeedItemsRepository implements ReadFeedItemsRepository {

    @Override
    public Set<String> load(final UUID feedId) {
        assertNotNull(feedId);

        final Entity entity = loadEntity(feedId, KIND, false);

        return entity == null ? new HashSet<String>() : convert(entity);
    }

    @Override
    public void store(final UUID feedId, final Set<String> itemIds) {
        assertNotNull(feedId);
        assertNotNull(itemIds);

        final Key feedRootKey = getFeedRootKey(feedId);
        final Entity entity = convert(feedRootKey, feedId, itemIds);

        DATASTORE_SERVICE.put(entity);
    }

    @Override
    public void delete(final UUID feedId) {
        assertNotNull(feedId);

        deleteEntity(feedId, KIND);
    }

}
