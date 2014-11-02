package nmd.orb.repositories;

import nmd.orb.collector.scheduler.FeedUpdateTaskSchedulerContext;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 30.04.13
 */
public interface FeedUpdateTaskSchedulerContextRepository {

    void store(FeedUpdateTaskSchedulerContext context);

    FeedUpdateTaskSchedulerContext load();

    void clear();

}
