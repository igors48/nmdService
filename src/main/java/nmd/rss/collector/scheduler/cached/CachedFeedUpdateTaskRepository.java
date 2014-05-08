package nmd.rss.collector.scheduler.cached;

import nmd.rss.collector.Cache;
import nmd.rss.collector.scheduler.FeedUpdateTask;
import nmd.rss.collector.scheduler.FeedUpdateTaskRepository;

import java.util.List;
import java.util.ListIterator;
import java.util.UUID;
import java.util.logging.Logger;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 06.03.14
 */
public class CachedFeedUpdateTaskRepository implements FeedUpdateTaskRepository {

    private static final Logger LOGGER = Logger.getLogger(CachedFeedUpdateTaskRepository.class.getName());

    public static final String KEY = "FeedUpdateTasks";

    private final FeedUpdateTaskRepository repository;
    private final Cache cache;

    public CachedFeedUpdateTaskRepository(final FeedUpdateTaskRepository repository, final Cache cache) {
        assertNotNull(repository);
        this.repository = repository;

        assertNotNull(cache);
        this.cache = cache;
    }

    @Override
    public synchronized List<FeedUpdateTask> loadAllTasks() {
        final List<FeedUpdateTask> cached = (List<FeedUpdateTask>) this.cache.get(KEY);

        return cached == null ? loadCache() : cached;
    }

    @Override
    public synchronized void storeTask(FeedUpdateTask feedUpdateTask) {
        assertNotNull(feedUpdateTask);

        this.repository.storeTask(feedUpdateTask);

        updateTaskInCache(feedUpdateTask);
    }

    @Override
    public synchronized FeedUpdateTask loadTaskForFeedId(UUID feedId) {
        assertNotNull(feedId);

        final List<FeedUpdateTask> tasks = loadAllTasks();

        for (final FeedUpdateTask task : tasks) {

            if (task.feedId.equals(feedId)) {
                return task;
            }
        }

        return null;
    }

    @Override
    public synchronized void deleteTaskForFeedId(UUID feedId) {
        assertNotNull(feedId);

        this.repository.deleteTaskForFeedId(feedId);

        this.cache.delete(KEY);
    }

    private List<FeedUpdateTask> loadCache() {
        final List<FeedUpdateTask> tasks = this.repository.loadAllTasks();

        this.cache.put(KEY, tasks);

        LOGGER.info("Feed update tasks were loaded from datastore");

        return tasks;
    }

    private void updateTaskInCache(final FeedUpdateTask feedUpdateTask) {
        final List<FeedUpdateTask> cached = loadAllTasks();

        for (final ListIterator<FeedUpdateTask> iterator = cached.listIterator(); iterator.hasNext(); ) {
            final FeedUpdateTask current = iterator.next();

            if (current.feedId.equals(feedUpdateTask.feedId)) {
                iterator.set(feedUpdateTask);
                this.cache.put(KEY, cached);

                return;
            }

        }

        cached.add(feedUpdateTask);
        this.cache.put(KEY, cached);
    }

}
