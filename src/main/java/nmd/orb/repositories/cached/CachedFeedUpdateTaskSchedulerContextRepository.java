package nmd.orb.repositories.cached;

import nmd.orb.collector.scheduler.FeedUpdateTaskSchedulerContext;
import nmd.orb.repositories.Cache;
import nmd.orb.repositories.FeedUpdateTaskSchedulerContextRepository;

import static nmd.orb.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date: 14.10.13
 */
public class CachedFeedUpdateTaskSchedulerContextRepository implements FeedUpdateTaskSchedulerContextRepository {

    private static final String KEY = "FeedUpdateTaskSchedulerContext";

    private final Cache cache;

    public CachedFeedUpdateTaskSchedulerContextRepository(final Cache cache) {
        this.cache = cache;
    }

    @Override
    public synchronized void store(final FeedUpdateTaskSchedulerContext context) {
        assertNotNull(context);

        this.cache.put(KEY, context);
    }

    @Override
    public synchronized FeedUpdateTaskSchedulerContext load() {
        final FeedUpdateTaskSchedulerContext context = (FeedUpdateTaskSchedulerContext) this.cache.get(KEY);

        return context == null ? FeedUpdateTaskSchedulerContext.START_CONTEXT : context;
    }

    @Override
    public synchronized void clear() {
        this.cache.delete(KEY);
    }

}
