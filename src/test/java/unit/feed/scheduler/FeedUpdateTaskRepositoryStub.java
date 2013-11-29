package unit.feed.scheduler;

import nmd.rss.collector.scheduler.FeedUpdateTask;
import nmd.rss.collector.scheduler.FeedUpdateTaskRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 30.04.13
 */
public class FeedUpdateTaskRepositoryStub implements FeedUpdateTaskRepository {

    private final List<FeedUpdateTask> tasks;

    public FeedUpdateTaskRepositoryStub(final FeedUpdateTask... tasks) {
        this.tasks = new ArrayList<>();

        for (FeedUpdateTask task : tasks) {
            storeTask(task);
        }
    }

    @Override
    public List<FeedUpdateTask> loadAllTasks() {
        return this.tasks;
    }

    @Override
    public void storeTask(final FeedUpdateTask feedUpdateTask) {
        this.tasks.add(feedUpdateTask);
    }

    @Override
    public FeedUpdateTask loadTaskForFeedId(final UUID feedId) {

        for (final FeedUpdateTask candidate : this.tasks) {

            if (candidate.feedId.equals(feedId)) {
                return candidate;
            }
        }

        return null;
    }

    @Override
    public void deleteTaskForFeedId(final UUID feedId) {

        for (final Iterator<FeedUpdateTask> iterator = this.tasks.iterator(); iterator.hasNext(); ) {
            final FeedUpdateTask candidate = iterator.next();

            if (candidate.feedId.equals(feedId)) {
                iterator.remove();
            }
        }
    }

    public boolean isEmpty() {
        return this.tasks.isEmpty();
    }

}
