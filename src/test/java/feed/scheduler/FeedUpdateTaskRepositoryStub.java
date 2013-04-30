package feed.scheduler;

import nmd.rss.collector.scheduler.FeedUpdateTask;
import nmd.rss.collector.scheduler.FeedUpdateTaskRepository;
import nmd.rss.collector.scheduler.FeedUpdateTaskRepositoryException;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 30.04.13
 */
public class FeedUpdateTaskRepositoryStub implements FeedUpdateTaskRepository {

    @Override
    public List<FeedUpdateTask> loadAllTasks() throws FeedUpdateTaskRepositoryException {
        return new ArrayList<>();
    }

}
