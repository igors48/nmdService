package unit.feed.controller;

import nmd.rss.collector.scheduler.FeedUpdateTask;
import nmd.rss.collector.scheduler.FeedUpdateTaskScheduler;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 02.05.13
 */
public class FeedUpdateTaskSchedulerStub implements FeedUpdateTaskScheduler {

    private FeedUpdateTask task;

    @Override
    public FeedUpdateTask getCurrentTask() {
        return this.task;
    }

    public void setTask(final FeedUpdateTask task) {
        this.task = task;
    }

}
