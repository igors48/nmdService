package feed.scheduler;

import nmd.rss.collector.scheduler.FeedUpdateTaskSchedulerContext;
import nmd.rss.collector.scheduler.FeedUpdateTaskSchedulerContextService;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 30.04.13
 */
public class FeedUpdateTaskSchedulerContextServiceStub implements FeedUpdateTaskSchedulerContextService {

    private FeedUpdateTaskSchedulerContext context;

    public FeedUpdateTaskSchedulerContextServiceStub() {
        this.context = null;
    }

    @Override
    public void store(final FeedUpdateTaskSchedulerContext context) {
        assertNotNull(context);
        this.context = context;
    }

    @Override
    public FeedUpdateTaskSchedulerContext load() {
        return this.context;
    }

}
