package nmd.rss.reader;

import nmd.rss.collector.Cache;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import static java.lang.String.format;
import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 08.03.14
 */
public class CachedReadFeedItemsRepository implements ReadFeedItemsRepository {

    private static final Logger LOGGER = Logger.getLogger(CachedReadFeedItemsRepository.class.getName());

    private final ReadFeedItemsRepository repository;
    private final Cache cache;

    public CachedReadFeedItemsRepository(final ReadFeedItemsRepository repository, final Cache cache) {
        assertNotNull(repository);
        this.repository = repository;

        assertNotNull(cache);
        this.cache = cache;
    }

    public static String keyFor(final UUID uuid) {
        assertNotNull(uuid);

        return "READ-" + uuid;
    }

    @Override
    public List<ReadFeedItems> loadAll() {
        //TODO tests
        return null;
    }

    @Override
    public ReadFeedItems load(final UUID feedId) {
        assertNotNull(feedId);

        final ReadFeedItems items;

        final String key = keyFor(feedId);

        final ReadFeedItems cached = (ReadFeedItems) this.cache.get(key);

        if (cached == null) {
            final ReadFeedItems stored = this.repository.load(feedId);
            this.cache.put(key, stored);

            items = stored;

            LOGGER.info(format("Read feed [ %s ] items was taken from datastore", feedId));
        } else {
            items = cached;
        }

        return items;
    }

    @Override
    public void store(final ReadFeedItems readFeedItems) {
        assertNotNull(readFeedItems);

        this.cache.put(keyFor(readFeedItems.feedId), readFeedItems);
        this.repository.store(readFeedItems);
    }

    @Override
    public void delete(final UUID feedId) {
        assertNotNull(feedId);

        this.cache.delete(keyFor(feedId));
        this.repository.delete(feedId);
    }

}
