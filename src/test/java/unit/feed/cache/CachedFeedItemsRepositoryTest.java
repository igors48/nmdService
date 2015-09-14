package unit.feed.cache;

import nmd.orb.feed.FeedItem;
import nmd.orb.repositories.cached.CachedFeedItemsRepository;
import org.junit.Before;
import org.junit.Test;
import unit.feed.controller.stub.FeedItemsRepositoryStub;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static nmd.orb.repositories.cached.CachedFeedItemsRepository.keyForItems;
import static org.junit.Assert.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 08.03.14
 */
public class CachedFeedItemsRepositoryTest {

    private static final UUID FEED_ID = randomUUID();

    private static final String ITEM_ID = randomUUID().toString();

    private static final FeedItem STORED = new FeedItem("stored-title", "stored-description", "http://domain.com/stored", "http://domain.com/storedGoto", new Date(1), true, ITEM_ID);
    private static final ArrayList<FeedItem> STORED_ITEMS = new ArrayList<FeedItem>() {{
        add(STORED);
    }};

    private static final FeedItem CACHED = new FeedItem("cached-title", "cached-description", "http://domain.com/cached", "http://domain.com/cachedGoto", new Date(1), true, ITEM_ID);
    private static final ArrayList<FeedItem> CACHED_ITEMS = new ArrayList<FeedItem>() {{
        add(CACHED);
    }};

    private FeedItemsRepositoryStub feedItemsRepositoryStub;
    private CacheStub cacheStub;

    private CachedFeedItemsRepository repository;

    @Before
    public void setUp() {
        this.cacheStub = new CacheStub();
        this.cacheStub.put(keyForItems(FEED_ID), CACHED_ITEMS);

        this.feedItemsRepositoryStub = new FeedItemsRepositoryStub();
        this.feedItemsRepositoryStub.storeItems(FEED_ID, STORED_ITEMS);

        this.repository = new CachedFeedItemsRepository(this.feedItemsRepositoryStub, this.cacheStub);
    }

    @Test
    public void whenItemIsStoredThenCacheIsUpdated() {
        this.repository.storeItems(FEED_ID, STORED_ITEMS);

        assertEquals(STORED_ITEMS, this.cacheStub.get(keyForItems(FEED_ID)));
    }

    @Test
    public void whenItemIsCachedThenItIsReturnedFromCache() {
        final List<FeedItem> items = this.repository.loadItems(FEED_ID);

        assertEquals(CACHED_ITEMS, items);
    }

    @Test
    public void whenItemIsNotCachedThenItIsReturnedFromDatastore() {
        this.cacheStub.clear();

        final List<FeedItem> items = this.repository.loadItems(FEED_ID);

        assertEquals(STORED_ITEMS, items);
    }

    @Test
    public void whenItemIsNotCachedThenCacheIsUpdated() {
        this.cacheStub.clear();

        this.repository.loadItems(FEED_ID);

        assertEquals(STORED_ITEMS, this.cacheStub.get(keyForItems(FEED_ID)));
    }

    @Test
    public void whenFeedIdIsNotExistsThenNullIsReturned() {
        assertNull(this.repository.loadItems(randomUUID()));
    }

    @Test
    public void whenItemIsDeletedThenCacheIsUpdated() {
        this.repository.deleteItems(FEED_ID);

        assertTrue(this.cacheStub.isEmpty());
    }

}
