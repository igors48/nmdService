package nmd.rss.collector.scheduler;

import nmd.rss.collector.Repository;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 30.04.13
 */
public interface FeedUpdateTaskSchedulerContextRepository extends Repository {

    void store(FeedUpdateTaskSchedulerContext context);

    FeedUpdateTaskSchedulerContext load();

}
