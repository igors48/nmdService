package nmd.orb.collector.scheduler;

import java.io.Serializable;

import static nmd.orb.util.Assert.assertPositive;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 30.04.13
 */
public class FeedUpdateTaskSchedulerContext implements Serializable {

    public static final FeedUpdateTaskSchedulerContext START_CONTEXT = new FeedUpdateTaskSchedulerContext(0);

    public final int lastTaskIndex;

    public FeedUpdateTaskSchedulerContext(final int lastTaskIndex) {
        assertPositive(lastTaskIndex);
        this.lastTaskIndex = lastTaskIndex;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FeedUpdateTaskSchedulerContext that = (FeedUpdateTaskSchedulerContext) o;

        if (lastTaskIndex != that.lastTaskIndex) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return lastTaskIndex;
    }

}
