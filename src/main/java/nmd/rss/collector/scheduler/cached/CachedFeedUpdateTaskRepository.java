package nmd.rss.collector.scheduler.cached;

import nmd.rss.collector.Cache;
import nmd.rss.collector.scheduler.FeedUpdateTask;
import nmd.rss.collector.scheduler.FeedUpdateTaskRepository;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import static nmd.rss.collector.util.Assert.guard;
import static nmd.rss.collector.util.Parameter.isPositive;
import static nmd.rss.collector.util.Parameter.notNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 06.03.14
 */
public class CachedFeedUpdateTaskRepository implements FeedUpdateTaskRepository {

    private static final Logger LOGGER = Logger.getLogger(CachedFeedUpdateTaskRepository.class.getName());

    public static final String KEY = "FeedUpdateTasks";

    private final FeedUpdateTaskRepository repository;
    private final int maxCacheWritesBeforeFlush;
    private final Cache cache;

    public CachedFeedUpdateTaskRepository(final FeedUpdateTaskRepository repository, final int maxCacheWritesBeforeFlush, final Cache cache) {
        guard(notNull(repository));
        this.repository = repository;

        guard(isPositive(maxCacheWritesBeforeFlush));
        this.maxCacheWritesBeforeFlush = maxCacheWritesBeforeFlush;

        guard(notNull(cache));
        this.cache = cache;
    }

    @Override
    public synchronized List<FeedUpdateTask> loadAllTasks() {
        final CachedFeedUpdateTasks cachedFeedUpdateTasks = getCachedTasks();

        return cachedFeedUpdateTasks.getTasks();
    }

    @Override
    public synchronized void storeTask(final FeedUpdateTask feedUpdateTask) {
        guard(notNull(feedUpdateTask));

        this.repository.storeTask(feedUpdateTask);

        updateCache(feedUpdateTask);
    }

    @Override
    public synchronized void updateTask(final FeedUpdateTask feedUpdateTask) {
        guard(notNull(feedUpdateTask));

        final CachedFeedUpdateTasks cachedFeedUpdateTasks = updateCache(feedUpdateTask);

        if (cachedFeedUpdateTasks.flushNeeded()) {
            flush(cachedFeedUpdateTasks);
        }
    }

    private CachedFeedUpdateTasks updateCache(FeedUpdateTask feedUpdateTask) {
        final CachedFeedUpdateTasks cachedFeedUpdateTasks = getCachedTasks();
        cachedFeedUpdateTasks.addOrUpdate(feedUpdateTask);

        this.cache.put(KEY, cachedFeedUpdateTasks);
        return cachedFeedUpdateTasks;
    }

    @Override
    public synchronized FeedUpdateTask loadTaskForFeedId(final UUID feedId) {
        guard(notNull(feedId));

        final CachedFeedUpdateTasks cachedFeedUpdateTasks = getCachedTasks();
        final List<FeedUpdateTask> tasks = cachedFeedUpdateTasks.getTasks();

        for (final FeedUpdateTask task : tasks) {

            if (task.feedId.equals(feedId)) {
                return task;
            }
        }

        return null;
    }

    @Override
    public synchronized void deleteTaskForFeedId(final UUID feedId) {
        guard(notNull(feedId));

        this.repository.deleteTaskForFeedId(feedId);

        this.cache.delete(KEY);
    }

    private CachedFeedUpdateTasks getCachedTasks() {
        CachedFeedUpdateTasks tasks = (CachedFeedUpdateTasks) this.cache.get(KEY);

        if (tasks == null) {
            final List<FeedUpdateTask> stored = this.repository.loadAllTasks();
            tasks = new CachedFeedUpdateTasks(stored, this.maxCacheWritesBeforeFlush);

            this.cache.put(KEY, tasks);

            LOGGER.info("Feed update tasks were loaded from datastore");
        }

        return tasks;
    }

    private void flush(final CachedFeedUpdateTasks cachedFeedUpdateTasks) {
        final List<FeedUpdateTask> tasks = cachedFeedUpdateTasks.getTasks();

        for (final FeedUpdateTask task : tasks) {
            this.repository.storeTask(task);
        }

        cachedFeedUpdateTasks.resetWritesCounter();

        LOGGER.info("Cached feed update tasks were flushed to datastore");
    }

}
