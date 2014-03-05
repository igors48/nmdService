package nmd.rss.collector.gae.updater;

import nmd.rss.collector.Cache;
import nmd.rss.collector.scheduler.FeedUpdateTaskSchedulerContext;
import nmd.rss.collector.scheduler.FeedUpdateTaskSchedulerContextRepository;

import static nmd.rss.collector.gae.cache.GaeCache.GAE_CACHE;
import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * User: igu
 * Date: 14.10.13
 */
public class GaeCacheFeedUpdateTaskSchedulerContextRepository implements FeedUpdateTaskSchedulerContextRepository {

    public static final FeedUpdateTaskSchedulerContextRepository GAE_FEED_UPDATE_TASK_SCHEDULER_CONTEXT_REPOSITORY = new GaeCacheFeedUpdateTaskSchedulerContextRepository(GAE_CACHE);

    private static final String KEY = "FeedUpdateTaskSchedulerContext";

    private final Cache cache;

    private GaeCacheFeedUpdateTaskSchedulerContextRepository(final Cache cache) {
        this.cache = cache;
    }

    @Override
    public void store(final FeedUpdateTaskSchedulerContext context) {
        assertNotNull(context);

        this.cache.put(KEY, context);
    }

    @Override
    public FeedUpdateTaskSchedulerContext load() {
        final FeedUpdateTaskSchedulerContext context = (FeedUpdateTaskSchedulerContext) this.cache.get(KEY);

        return context == null ? FeedUpdateTaskSchedulerContext.START_CONTEXT : context;
    }

    @Override
    public void clear() {
        this.cache.delete(KEY);
    }

}
