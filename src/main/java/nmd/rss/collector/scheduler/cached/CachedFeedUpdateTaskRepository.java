package nmd.rss.collector.scheduler.cached;

import nmd.rss.collector.Cache;
import nmd.rss.collector.scheduler.FeedUpdateTask;
import nmd.rss.collector.scheduler.FeedUpdateTaskRepository;

import java.util.List;
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
    public List<FeedUpdateTask> loadAllTasks() {
        final List<FeedUpdateTask> cached = (List<FeedUpdateTask>) this.cache.get(KEY);

        return cached == null ? loadCache() : cached;
    }

    @Override
    public void storeTask(FeedUpdateTask feedUpdateTask) {
        assertNotNull(feedUpdateTask);

        this.repository.storeTask(feedUpdateTask);

        this.cache.delete(KEY);
    }

    @Override
    public FeedUpdateTask loadTaskForFeedId(UUID feedId) {
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
    public void deleteTaskForFeedId(UUID feedId) {
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

}
