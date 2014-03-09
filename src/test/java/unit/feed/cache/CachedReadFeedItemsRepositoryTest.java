package unit.feed.cache;

import nmd.rss.reader.CachedReadFeedItemsRepository;
import nmd.rss.reader.ReadFeedItems;
import org.junit.Before;
import org.junit.Test;
import unit.feed.controller.ReadFeedItemsRepositoryStub;

import java.util.Date;
import java.util.HashSet;
import java.util.UUID;

import static nmd.rss.reader.CachedReadFeedItemsRepository.keyFor;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 08.03.14
 */
public class CachedReadFeedItemsRepositoryTest {

    private static final UUID FEED_ID = UUID.randomUUID();

    private static final ReadFeedItems CACHED = new ReadFeedItems(new Date(1), new HashSet<String>(), new HashSet<String>(), "cached");
    private static final ReadFeedItems STORED = new ReadFeedItems(new Date(2), new HashSet<String>(), new HashSet<String>(), "stored");

    private CachedReadFeedItemsRepository repository;

    private ReadFeedItemsRepositoryStub readFeedItemsRepositoryStub;
    private CacheStub cacheStub;

    @Before
    public void setUp() {
        this.cacheStub = new CacheStub();
        this.cacheStub.put(keyFor(FEED_ID), CACHED);

        this.readFeedItemsRepositoryStub = new ReadFeedItemsRepositoryStub();
        this.readFeedItemsRepositoryStub.store(FEED_ID, STORED);

        this.repository = new CachedReadFeedItemsRepository(this.readFeedItemsRepositoryStub, this.cacheStub);
    }

    @Test
    public void whenItemWasCachedThenItIsReturnedFromCache() {
        final ReadFeedItems items = this.repository.load(FEED_ID);

        assertEquals(CACHED, items);
    }

    @Test
    public void whenItemWasNotCachedThenItIsReturnedFromRepository() {
        this.cacheStub.clear();

        final ReadFeedItems items = this.repository.load(FEED_ID);

        assertEquals(STORED, items);
    }

    @Test
    public void whenItemWasNotCachedThenCacheIsUpdatedAfterLoad() {
        this.cacheStub.clear();

        this.repository.load(FEED_ID);

        assertEquals(STORED, this.cacheStub.get(keyFor(FEED_ID)));
    }

    @Test
    public void whenItemIsStoredThenCacheIsUpdated() {
        this.repository.store(FEED_ID, STORED);

        assertEquals(STORED, this.cacheStub.get(keyFor(FEED_ID)));
    }

    @Test
    public void whenItemIsDeletedThenCacheIsUpdated() {
        this.repository.delete(FEED_ID);

        assertNull(this.cacheStub.get(keyFor(FEED_ID)));
    }

}
