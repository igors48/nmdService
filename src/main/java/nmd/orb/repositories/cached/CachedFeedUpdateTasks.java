package nmd.orb.repositories.cached;

import nmd.orb.collector.scheduler.FeedUpdateTask;

import java.io.Serializable;
import java.util.List;
import java.util.ListIterator;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

public class CachedFeedUpdateTasks implements Serializable {

    private final List<FeedUpdateTask> tasks;

    public CachedFeedUpdateTasks(final List<FeedUpdateTask> tasks) {
        guard(notNull(tasks));
        this.tasks = tasks;
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
    }

    public List<FeedUpdateTask> getTasks() {
        return this.tasks;
    }

}
