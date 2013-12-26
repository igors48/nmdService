package nmd.rss.reader.gae;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import nmd.rss.reader.ReadFeedItems;
import nmd.rss.reader.ReadFeedItemsRepository;

import java.util.UUID;

import static nmd.rss.collector.gae.persistence.GaeRootRepository.*;
import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.reader.ReadFeedItems.EMPTY;
import static nmd.rss.reader.gae.ReadFeedItemsConverter.KIND;
import static nmd.rss.reader.gae.ReadFeedItemsConverter.convert;

/**
 * User: igu
 * Date: 26.11.13
 */
public class GaeReadFeedItemsRepository implements ReadFeedItemsRepository {

    @Override
    public ReadFeedItems load(final UUID feedId) {
        assertNotNull(feedId);

        final Entity entity = loadEntity(feedId, KIND, false);

        return entity == null ? EMPTY : convert(entity);
    }

    @Override
    public void store(final UUID feedId, final ReadFeedItems readFeedItems) {
        assertNotNull(feedId);
        assertNotNull(readFeedItems);

        delete(feedId);

        final Key feedRootKey = getFeedRootKey(feedId);
        final Entity entity = convert(feedRootKey, feedId, readFeedItems);

        DATASTORE_SERVICE.put(entity);
    }

    @Override
    public void delete(final UUID feedId) {
        assertNotNull(feedId);

        deleteEntity(feedId, KIND);
    }

}
