package nmd.rss.collector.scheduler;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 30.04.13
 */
public interface FeedUpdateTaskSchedulerContextService {

    void store(FeedUpdateTaskSchedulerContext context) throws FeedUpdateTaskSchedulerContextServiceException;

    FeedUpdateTaskSchedulerContext load() throws FeedUpdateTaskSchedulerContextServiceException;

}
