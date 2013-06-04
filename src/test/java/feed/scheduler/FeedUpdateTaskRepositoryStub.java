package feed.scheduler;

import nmd.rss.collector.scheduler.FeedUpdateTask;
import nmd.rss.collector.scheduler.FeedUpdateTaskRepository;

import java.util.ArrayList;
import java.util.List;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 30.04.13
 */
public class FeedUpdateTaskRepositoryStub implements FeedUpdateTaskRepository {

    private final List<FeedUpdateTask> tasks;

    public FeedUpdateTaskRepositoryStub(final FeedUpdateTask... tasks) {
        assertNotNull(tasks);

        this.tasks = new ArrayList<>();

        for (FeedUpdateTask task : tasks) {
            this.tasks.add(task);
        }
    }

    public void addTask(final FeedUpdateTask task) {
        assertNotNull(task);
        this.tasks.add(task);
    }

    public void removeTask(final FeedUpdateTask task) {
        assertNotNull(task);
        this.tasks.remove(task);
    }

    @Override
    public List<FeedUpdateTask> loadAllTasks() {
        return this.tasks;
    }

    @Override
    public void storeTask(final FeedUpdateTask feedUpdateTask) {

    }

}
