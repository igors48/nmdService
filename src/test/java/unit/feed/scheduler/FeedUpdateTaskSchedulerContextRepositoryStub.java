package unit.feed.scheduler;

import nmd.orb.collector.scheduler.FeedUpdateTaskSchedulerContext;
import nmd.orb.repositories.FeedUpdateTaskSchedulerContextRepository;

import static nmd.orb.util.Assert.assertNotNull;

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

    @Override
    public void clear() {
        this.context = null;
    }

    public boolean isEmpty() {
        return this.context == null;
    }

}
