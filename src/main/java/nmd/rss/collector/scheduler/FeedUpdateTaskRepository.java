package nmd.rss.collector.scheduler;

import nmd.rss.collector.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 30.04.13
 */
public interface FeedUpdateTaskRepository extends Repository {

    List<FeedUpdateTask> loadAllTasks();

    void storeTask(FeedUpdateTask feedUpdateTask);

    FeedUpdateTask loadTaskForFeedId(UUID feedId);

    void deleteTaskForFeedId(UUID feedId);

}
