package unit.feed.cache;

import nmd.orb.collector.scheduler.FeedUpdateTask;
import nmd.orb.repositories.cached.CachedFeedUpdateTaskRepository;
import nmd.orb.repositories.cached.CachedFeedUpdateTasks;
import org.junit.Before;
import org.junit.Test;
import unit.feed.scheduler.FeedUpdateTaskRepositoryStub;

import java.util.List;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.junit.Assert.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 07.03.14
 */
public class CachedFeedUpdateTaskRepositoryTest {

    private static final UUID STORED_FEED_ID = randomUUID();

    private static final FeedUpdateTask CACHED = new FeedUpdateTask(STORED_FEED_ID, 48);
    private static final FeedUpdateTask STORED = new FeedUpdateTask(STORED_FEED_ID, 49);

    private CachedFeedUpdateTaskRepository repository;

    private FeedUpdateTaskRepositoryStub feedUpdateTaskRepositoryStub;
    private CacheStub cacheStub;

    @Before
    public void setUp() {
        this.cacheStub = new CacheStub();

        this.feedUpdateTaskRepositoryStub = new FeedUpdateTaskRepositoryStub();
        this.feedUpdateTaskRepositoryStub.storeTask(STORED);

        this.repository = new CachedFeedUpdateTaskRepository(this.feedUpdateTaskRepositoryStub, this.cacheStub);
    }

    @Test
    public void whenListIsCachedThenItemsAreTakenFromCache() {
        this.feedUpdateTaskRepositoryStub.storeTask(CACHED);
        this.repository.loadAllTasks();

        this.feedUpdateTaskRepositoryStub.storeTask(STORED);
        final List<FeedUpdateTask> tasks = this.repository.loadAllTasks();

        assertEquals(1, tasks.size());
        assertEquals(CACHED, tasks.get(0));
    }

    @Test
    public void whenListIsNotCachedThenItemsAreTakenFromRepository() {
        this.cacheStub.clear();

        final List<FeedUpdateTask> tasks = this.repository.loadAllTasks();

        assertEquals(1, tasks.size());
        assertEquals(STORED, tasks.get(0));
    }

    @Test
    public void whenTaskIsStoredThenCacheIsUpdated() {
        this.repository.storeTask(STORED);

        final CachedFeedUpdateTasks tasks = (CachedFeedUpdateTasks) this.cacheStub.get(CachedFeedUpdateTaskRepository.KEY);

        assertEquals(1, tasks.getTasks().size());
        assertTrue(tasks.getTasks().contains(STORED));
    }

    @Test
    public void whenFeedIdIsValidThenTaskWillBeReturned() {
        final FeedUpdateTask task = this.repository.loadTaskForFeedId(STORED_FEED_ID);

        assertNotNull(task);
    }

    @Test
    public void whenFeedIdIsNotValidThenNullWillBeReturned() {
        final FeedUpdateTask task = this.repository.loadTaskForFeedId(randomUUID());

        assertNull(task);
    }

    @Test
    public void whenTaskIsDeletedThenCacheIsCleared() {
        this.repository.deleteTaskForFeedId(STORED_FEED_ID);

        assertTrue(this.cacheStub.isEmpty());
    }

}
