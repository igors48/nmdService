package unit.feed.cache;

import nmd.orb.feed.FeedItem;
import nmd.orb.feed.FeedItemShortcut;
import nmd.orb.repositories.cached.CachedFeedItemsRepository;
import org.junit.Before;
import org.junit.Test;
import unit.feed.controller.stub.FeedItemsRepositoryStub;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static nmd.orb.feed.FeedItem.createShortcut;
import static nmd.orb.repositories.cached.CachedFeedItemsRepository.keyForItems;
import static nmd.orb.repositories.cached.CachedFeedItemsRepository.keyForShortcuts;
import static org.junit.Assert.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 08.03.14
 */
public class CachedFeedItemsRepositoryTest {

    private static final UUID FEED_ID = randomUUID();

    private static final String ITEM_ID = randomUUID().toString();

    private static final FeedItem STORED_ITEM = new FeedItem("stored-title", "stored-description", "http://domain.com/stored", "http://domain.com/storedGoto", new Date(1), true, ITEM_ID);
    private static final ArrayList<FeedItem> STORED_ITEMS = new ArrayList<FeedItem>() {{
        add(STORED_ITEM);
    }};

    private static final FeedItemShortcut STORED_SHORTCUT = createShortcut(STORED_ITEM);
    private static final ArrayList<FeedItemShortcut> STORED_SHORTCUTS = new ArrayList<FeedItemShortcut>() {{
        add(STORED_SHORTCUT);
    }};

    private static final FeedItem CACHED_ITEM = new FeedItem("cached-title", "cached-description", "http://domain.com/cached", "http://domain.com/cachedGoto", new Date(1), true, ITEM_ID);
    private static final ArrayList<FeedItem> CACHED_ITEMS = new ArrayList<FeedItem>() {{
        add(CACHED_ITEM);
    }};

    private static final FeedItemShortcut CACHED_SHORTCUT = createShortcut(CACHED_ITEM);
    private static final ArrayList<FeedItemShortcut> CACHED_SHORTCUTS = new ArrayList<FeedItemShortcut>() {{
        add(CACHED_SHORTCUT);
    }};

    private CacheStub cacheStub;

    private CachedFeedItemsRepository repository;

    @Before
    public void setUp() {
        this.cacheStub = new CacheStub();
        this.cacheStub.put(keyForItems(FEED_ID), CACHED_ITEMS);
        this.cacheStub.put(keyForShortcuts(FEED_ID), CACHED_SHORTCUTS);

        final FeedItemsRepositoryStub feedItemsRepositoryStub = new FeedItemsRepositoryStub();
        feedItemsRepositoryStub.storeItems(FEED_ID, STORED_ITEMS);

        this.repository = new CachedFeedItemsRepository(feedItemsRepositoryStub, this.cacheStub);
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

    @Test
    public void whenShortcutIsStoredThenCacheIsUpdated() {
        this.repository.storeItems(FEED_ID, STORED_ITEMS);

        assertEquals(STORED_SHORTCUTS, this.cacheStub.get(keyForShortcuts(FEED_ID)));
    }

    @Test
    public void whenShortcutIsCachedThenItIsReturnedFromCache() {
        final List<FeedItemShortcut> shortcuts = this.repository.loadItemsShortcuts(FEED_ID);

        assertEquals(CACHED_SHORTCUTS, shortcuts);
    }

    @Test
    public void whenShortcutIsNotCachedThenItIsReturnedFromDatastore() {
        this.cacheStub.clear();

        final List<FeedItemShortcut> shortcuts = this.repository.loadItemsShortcuts(FEED_ID);

        assertEquals(STORED_SHORTCUTS, shortcuts);
    }

    @Test
    public void whenShortcutIsNotCachedThenCacheIsUpdated() {
        this.cacheStub.clear();

        this.repository.loadItemsShortcuts(FEED_ID);

        assertEquals(STORED_SHORTCUTS, this.cacheStub.get(keyForShortcuts(FEED_ID)));
    }

    @Test
    public void whenFeedIdIsNotExistsThenNullIsReturnedForShortcuts() {
        assertNull(this.repository.loadItemsShortcuts(randomUUID()));
    }

}
