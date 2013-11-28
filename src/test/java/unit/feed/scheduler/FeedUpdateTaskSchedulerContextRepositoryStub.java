package unit.feed.scheduler;

import nmd.rss.collector.scheduler.FeedUpdateTaskSchedulerContext;
import nmd.rss.collector.scheduler.FeedUpdateTaskSchedulerContextRepository;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 30.04.13
 */
public class FeedUpdateTaskSchedulerContextRepositoryStub implements FeedUpdateTaskSchedulerContextRepository {

    private FeedUpdateTaskSchedulerContext context;

    public FeedUpdateTaskSchedulerContextRepositoryStub() {
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
