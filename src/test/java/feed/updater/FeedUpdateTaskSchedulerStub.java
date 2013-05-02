package feed.updater;

import nmd.rss.collector.scheduler.FeedUpdateTask;
import nmd.rss.collector.scheduler.FeedUpdateTaskScheduler;
import nmd.rss.collector.scheduler.FeedUpdateTaskSchedulerException;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 02.05.13
 */
public class FeedUpdateTaskSchedulerStub implements FeedUpdateTaskScheduler {

    private FeedUpdateTask task;

    @Override
    public FeedUpdateTask getCurrentTask() throws FeedUpdateTaskSchedulerException {
        return this.task;
    }

    public void setTask(final FeedUpdateTask task) {
        this.task = task;
    }

}
