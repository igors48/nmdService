package nmd.rss.collector.scheduler;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 30.04.13
 */
public class FeedUpdateTaskScheduler {

    public FeedUpdateTask getCurrentTask() {
        return new FeedUpdateTask();
    }
}
