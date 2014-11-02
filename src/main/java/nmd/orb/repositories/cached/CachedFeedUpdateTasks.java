package nmd.orb.repositories.cached;

import nmd.orb.collector.scheduler.FeedUpdateTask;

import java.io.Serializable;
import java.util.List;
import java.util.ListIterator;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.isPositive;
import static nmd.orb.util.Parameter.notNull;

public class CachedFeedUpdateTasks implements Serializable {

    private final int maxWriteCount;
    private final List<FeedUpdateTask> tasks;

    private int updatesCount;

    public CachedFeedUpdateTasks(final List<FeedUpdateTask> tasks, final int maxWriteCount) {
        guard(notNull(tasks));
        this.tasks = tasks;

        guard(isPositive(maxWriteCount));
        this.maxWriteCount = maxWriteCount;

        resetWritesCounter();
    }

    public void addOrUpdate(final FeedUpdateTask feedUpdateTask) {
        guard(notNull(feedUpdateTask));

        boolean updated = false;

        final ListIterator<FeedUpdateTask> iterator = this.tasks.listIterator();

        while (iterator.hasNext()) {
            final FeedUpdateTask current = iterator.next();

            if (current.feedId.equals(feedUpdateTask.feedId)) {
                iterator.set(feedUpdateTask);
                updated = true;

                break;
            }

        }

        if (!updated) {
            iterator.add(feedUpdateTask);
        }

        ++this.updatesCount;
    }

    public boolean flushNeeded() {
        return this.updatesCount > this.maxWriteCount;
    }

    public List<FeedUpdateTask> getTasks() {
        return this.tasks;
    }

    public void resetWritesCounter() {
        this.updatesCount = 0;
    }

}
