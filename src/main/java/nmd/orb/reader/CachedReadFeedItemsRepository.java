package nmd.orb.reader;

import nmd.orb.collector.Cache;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import static nmd.orb.reader.ReadFeedItems.empty;
import static nmd.orb.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 08.03.14
 */
public class CachedReadFeedItemsRepository implements ReadFeedItemsRepository {

    public static final String KEY = "Reads";
    private static final Logger LOGGER = Logger.getLogger(CachedReadFeedItemsRepository.class.getName());

    private final ReadFeedItemsRepository repository;
    private final Cache cache;

    public CachedReadFeedItemsRepository(final ReadFeedItemsRepository repository, final Cache cache) {
        assertNotNull(repository);
        this.repository = repository;

        assertNotNull(cache);
        this.cache = cache;
    }

    @Override
    public synchronized List<ReadFeedItems> loadAll() {
        final List<ReadFeedItems> cached = (List<ReadFeedItems>) this.cache.get(KEY);

        return cached == null ? loadCache() : cached;
    }

    @Override
    public synchronized ReadFeedItems load(final UUID feedId) {
        assertNotNull(feedId);

        final List<ReadFeedItems> itemsList = loadAll();

        for (final ReadFeedItems items : itemsList) {

            if (items.feedId.equals(feedId)) {
                return items;
            }
        }

        return empty(feedId);
    }

    @Override
    public synchronized void store(final ReadFeedItems readFeedItems) {
        assertNotNull(readFeedItems);

        this.cache.delete(KEY);
        this.repository.store(readFeedItems);
    }

    @Override
    public synchronized void delete(final UUID feedId) {
        assertNotNull(feedId);

        this.cache.delete(KEY);
        this.repository.delete(feedId);
    }

    private List<ReadFeedItems> loadCache() {
        final List<ReadFeedItems> readFeedItemsList = this.repository.loadAll();

        this.cache.put(KEY, readFeedItemsList);

        LOGGER.info("Read items were loaded from datastore");

        return readFeedItemsList;
    }

}
