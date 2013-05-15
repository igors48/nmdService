package nmd.rss.collector.gae.updater;

import nmd.rss.collector.scheduler.FeedUpdateTaskSchedulerContext;
import nmd.rss.collector.scheduler.FeedUpdateTaskSchedulerContextRepository;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 15.05.13
 */
public class GaeFeedUpdateTaskSchedulerContextRepository implements FeedUpdateTaskSchedulerContextRepository {

    @Override
    public void store(final FeedUpdateTaskSchedulerContext context) {
        assertNotNull(context);
    }

    @Override
    public FeedUpdateTaskSchedulerContext load() {
        return null;
    }

}
