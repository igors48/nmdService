package unit.feed.cache;

import nmd.orb.reader.ReadFeedItems;
import nmd.orb.repositories.cached.CachedReadFeedItemsRepository;
import org.junit.Before;
import org.junit.Test;
import unit.feed.controller.stub.ReadFeedItemsRepositoryStub;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.UUID;

import static nmd.orb.repositories.cached.CachedReadFeedItemsRepository.KEY;
import static org.junit.Assert.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 08.03.14
 */
public class CachedReadFeedItemsRepositoryTest {

    private static final UUID FEED_ID = UUID.randomUUID();

    private static final ReadFeedItems CACHED = new ReadFeedItems(FEED_ID, new Date(1), new HashSet<String>(), new HashSet<String>(), "cached");
    private static final ReadFeedItems STORED = new ReadFeedItems(FEED_ID, new Date(2), new HashSet<String>(), new HashSet<String>(), "stored");

    private CachedReadFeedItemsRepository repository;

    private ReadFeedItemsRepositoryStub readFeedItemsRepositoryStub;
    private CacheStub cacheStub;

    @Before
    public void setUp() {
        this.cacheStub = new CacheStub();
        this.cacheStub.put(KEY, new ArrayList<ReadFeedItems>() {{
            add(CACHED);
        }});

        this.readFeedItemsRepositoryStub = new ReadFeedItemsRepositoryStub();
        this.readFeedItemsRepositoryStub.store(STORED);

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

        assertNotNull(this.cacheStub.get(KEY));
    }

    @Test
    public void whenItemIsStoredThenCacheIsDeleted() {
        this.repository.store(STORED);

        assertNull(this.cacheStub.get(KEY));
    }

    @Test
    public void whenItemIsDeletedThenCacheIsDeleted() {
        this.repository.delete(FEED_ID);

        assertNull(this.cacheStub.get(KEY));
    }

}
