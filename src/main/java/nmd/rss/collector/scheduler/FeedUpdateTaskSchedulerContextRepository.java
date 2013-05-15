package nmd.rss.collector.scheduler;

import java.util.List;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 30.04.13
 */
public interface FeedUpdateTaskSchedulerContextRepository {

    void store(FeedUpdateTaskSchedulerContext context);

    FeedUpdateTaskSchedulerContext load();

    List loadAllEntities();

    void deleteEntity(Object victim);

}
