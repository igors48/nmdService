package nmd.rss.collector.updater;

import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.collector.scheduler.FeedUpdateTask;
import nmd.rss.collector.scheduler.FeedUpdateTaskRepository;

import java.util.List;
import java.util.UUID;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 09.05.13
 */
public class InMemoryFeedHeadersAndUpdateTasksRepository implements FeedHeadersRepository, FeedUpdateTaskRepository {

    @Override
    public FeedHeader loadHeader(final UUID feedId) {
        assertNotNull(feedId);

        return null;
    }

    @Override
    public List<FeedUpdateTask> loadAllTasks() {
        return null;
    }

}
