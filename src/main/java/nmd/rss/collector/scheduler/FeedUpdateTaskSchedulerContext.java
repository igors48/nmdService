package nmd.rss.collector.scheduler;

import static nmd.rss.collector.util.Assert.assertPositive;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 30.04.13
 */
public class FeedUpdateTaskSchedulerContext {

    public static final FeedUpdateTaskSchedulerContext START_CONTEXT = new FeedUpdateTaskSchedulerContext(0);

    public final int lastTaskIndex;

    public FeedUpdateTaskSchedulerContext(final int lastTaskIndex) {
        assertPositive(lastTaskIndex);
        this.lastTaskIndex = lastTaskIndex;
    }

}
