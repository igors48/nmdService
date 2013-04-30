package nmd.rss.collector.scheduler;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 30.04.13
 */
public interface FeedUpdateTaskSchedulerContextRepository {

    void store(FeedUpdateTaskSchedulerContext context) throws FeedUpdateTaskSchedulerContextRepositoryException;

    FeedUpdateTaskSchedulerContext load() throws FeedUpdateTaskSchedulerContextRepositoryException;

}
