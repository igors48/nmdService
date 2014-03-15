package nmd.rss.reader.gae;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import nmd.rss.reader.ReadFeedItems;
import nmd.rss.reader.ReadFeedItemsRepository;

import java.util.List;
import java.util.UUID;

import static nmd.rss.collector.gae.persistence.GaeRootRepository.*;
import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.reader.ReadFeedItems.empty;
import static nmd.rss.reader.gae.ReadFeedItemsConverter.KIND;
import static nmd.rss.reader.gae.ReadFeedItemsConverter.convert;

/**
 * User: igu
 * Date: 26.11.13
 */
public class GaeReadFeedItemsRepository implements ReadFeedItemsRepository {

    public static final ReadFeedItemsRepository GAE_READ_FEED_ITEMS_REPOSITORY = new GaeReadFeedItemsRepository();

    @Override
    public List<ReadFeedItems> loadAll() {
        return null;
    }

    @Override
    public ReadFeedItems load(final UUID feedId) {
        assertNotNull(feedId);

        final Entity entity = loadEntity(feedId, KIND, false);

        return entity == null ? empty(feedId) : convert(entity);
    }

    @Override
    public void store(final ReadFeedItems readFeedItems) {
        assertNotNull(readFeedItems);

        delete(readFeedItems.feedId);

        final Key feedRootKey = getFeedRootKey(readFeedItems.feedId);
        final Entity entity = convert(feedRootKey, readFeedItems);

        DATASTORE_SERVICE.put(entity);
    }

    @Override
    public void delete(final UUID feedId) {
        assertNotNull(feedId);

        deleteEntity(feedId, KIND);
    }

    private GaeReadFeedItemsRepository() {
        // empty
    }

}
