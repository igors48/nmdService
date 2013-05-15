package nmd.rss.collector.scheduler;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 09.05.13
 */
public class InMemoryFeedUpdateTaskSchedulerContextRepository implements FeedUpdateTaskSchedulerContextRepository {

    private FeedUpdateTaskSchedulerContext context;

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
