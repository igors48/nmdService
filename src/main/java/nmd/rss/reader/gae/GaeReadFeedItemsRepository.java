package nmd.rss.reader.gae;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import nmd.rss.reader.ReadFeedItemsRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static nmd.rss.collector.gae.persistence.GaeRootRepository.DATASTORE_SERVICE;
import static nmd.rss.collector.gae.persistence.GaeRootRepository.getFeedRootKey;
import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.reader.gae.ReadFeedIdSetConverter.KIND;

/**
 * User: igu
 * Date: 26.11.13
 */
public class GaeReadFeedItemsRepository implements ReadFeedItemsRepository {

    @Override
    public Set<String> load(final UUID feedId) {
        assertNotNull(feedId);

        final Entity entity = loadEntity(feedId, false);

        return entity == null ? new HashSet<String>() : ReadFeedIdSetConverter.convert(entity);
    }

    @Override
    public void store(final UUID feedId, final Set<String> itemId) {
        assertNotNull(feedId);
        assertNotNull(itemId);

    }

    @Override
    public void delete(final UUID feedId) {
        assertNotNull(feedId);

    }

    //TODO consider generify and move to root repo
    private Entity loadEntity(final UUID feedId, final boolean keysOnly) {
        final Key feedRootKey = getFeedRootKey(feedId);
        final Query query = new Query(KIND).setAncestor(feedRootKey);

        if (keysOnly) {
            query.setKeysOnly();
        }

        final PreparedQuery preparedQuery = DATASTORE_SERVICE.prepare(query);

        return preparedQuery.asSingleEntity();
    }

}
