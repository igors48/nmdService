package nmd.rss.collector.gae.updater;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import nmd.rss.collector.scheduler.FeedUpdateTaskSchedulerContext;
import nmd.rss.collector.scheduler.FeedUpdateTaskSchedulerContextRepository;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * User: igu
 * Date: 14.10.13
 */
public class GaeCacheFeedUpdateTaskSchedulerContextRepository implements FeedUpdateTaskSchedulerContextRepository {

    private static final MemcacheService CACHE = MemcacheServiceFactory.getMemcacheService();
    private static final String KEY = "FeedUpdateTaskSchedulerContext";

    @Override
    public void store(FeedUpdateTaskSchedulerContext context) {
        assertNotNull(context);

        CACHE.put(KEY, context);
    }

    @Override
    public FeedUpdateTaskSchedulerContext load() {
        final FeedUpdateTaskSchedulerContext context = (FeedUpdateTaskSchedulerContext) CACHE.get(KEY);

        return context == null ? FeedUpdateTaskSchedulerContext.START_CONTEXT : context;
    }

}
