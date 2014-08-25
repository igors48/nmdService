package unit.feed.scheduler;

import nmd.orb.collector.scheduler.FeedUpdateTask;
import nmd.orb.collector.scheduler.FeedUpdateTaskRepository;

import java.util.ArrayList;
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
        return new ArrayList<>(this.tasks);
    }

    @Override
    public void storeTask(final FeedUpdateTask feedUpdateTask) {
        final int index = find(feedUpdateTask.feedId);

        if (index == -1) {
            this.tasks.add(feedUpdateTask);
        } else {
            this.tasks.set(index, feedUpdateTask);
        }
    }

    @Override
    public void updateTask(final FeedUpdateTask feedUpdateTask) {
        storeTask(feedUpdateTask);
    }

    @Override
    public FeedUpdateTask loadTaskForFeedId(final UUID feedId) {
        final int index = find(feedId);

        return index == -1 ? null : this.tasks.get(index);
    }

    @Override
    public void deleteTaskForFeedId(final UUID feedId) {
        final int index = find(feedId);

        if (index != -1) {
            this.tasks.remove(index);
        }
    }

    public boolean isEmpty() {
        return this.tasks.isEmpty();
    }

    private int find(final UUID feedId) {

        for (int index = 0; index < this.tasks.size(); ++index) {
            final FeedUpdateTask current = this.tasks.get(index);

            if (current.feedId.equals(feedId)) {
                return index;
            }
        }

        return -1;
    }

}
