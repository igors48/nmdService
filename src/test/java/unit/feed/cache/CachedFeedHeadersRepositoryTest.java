package unit.feed.cache;

import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.collector.updater.cached.CachedFeedHeadersRepository;
import org.junit.Before;
import org.junit.Test;
import unit.feed.controller.stub.FeedHeadersRepositoryStub;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.junit.Assert.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 07.03.14
 */
public class CachedFeedHeadersRepositoryTest {

    private static final UUID STORED_FEED_ID = randomUUID();

    private static final String STORED_FEED_LINK = "feed-link";
    private static final FeedHeader STORED = new FeedHeader(STORED_FEED_ID, STORED_FEED_LINK, "stored-feed-title", "stored-description", "stored-link");
    private static final FeedHeader CACHED = new FeedHeader(STORED_FEED_ID, STORED_FEED_LINK, "cached-feed-title", "cached-description", "cached-link");

    private CachedFeedHeadersRepository repository;

    private FeedHeadersRepositoryStub feedHeadersRepositoryStub;
    private CacheStub cacheStub;

    @Before
    public void setUp() {
        this.cacheStub = new CacheStub();
        this.cacheStub.put(CachedFeedHeadersRepository.KEY, new ArrayList<FeedHeader>() {{
            add(CACHED);
        }});

        this.feedHeadersRepositoryStub = new FeedHeadersRepositoryStub();
        this.feedHeadersRepositoryStub.storeHeader(STORED);

        this.repository = new CachedFeedHeadersRepository(this.feedHeadersRepositoryStub, this.cacheStub);
    }

    @Test
    public void whenListIsCachedThenItemsAreTakenFromCache() {
        final List<FeedHeader> headers = this.repository.loadHeaders();

        assertEquals(1, headers.size());
        assertEquals(CACHED, headers.get(0));
    }

    @Test
    public void whenListIsNotCachedThenItemsAreTakenFromRepository() {
        this.cacheStub.clear();

        final List<FeedHeader> headers = this.repository.loadHeaders();

        assertEquals(1, headers.size());
        assertEquals(STORED, headers.get(0));
    }

    @Test
    public void whenTaskIsStoredThenCacheIsCleared() {
        this.repository.storeHeader(STORED);

        assertTrue(this.cacheStub.isEmpty());
    }

    @Test
    public void whenTaskIsDeletedThenCacheIsCleared() {
        this.repository.deleteHeader(STORED_FEED_ID);

        assertTrue(this.cacheStub.isEmpty());
    }

    @Test
    public void whenFeedIdIsValidThenTaskWillBeReturned() {
        final FeedHeader header = this.repository.loadHeader(STORED_FEED_ID);

        assertNotNull(header);
    }

    @Test
    public void whenFeedIdIsNotValidThenTaskWillBeReturned() {
        final FeedHeader header = this.repository.loadHeader(randomUUID());

        assertNull(header);
    }

    @Test
    public void whenFeedLinkIsValidThenTaskWillBeReturned() {
        final FeedHeader header = this.repository.loadHeader(STORED_FEED_LINK);

        assertNotNull(header);
    }

    @Test
    public void whenFeedLinkIsNotValidThenTaskWillBeReturned() {
        final FeedHeader header = this.repository.loadHeader("*");

        assertNull(header);
    }

}
