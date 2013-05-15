package nmd.rss.collector.gae.updater;

import nmd.rss.collector.scheduler.FeedUpdateTaskSchedulerContext;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.collector.util.Assert.assertPositive;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 15.05.13
 */
@Entity
public class SchedulerContext {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private com.google.appengine.api.datastore.Key key;

    private int lastTaskIndex;

    private SchedulerContext() {
        this.lastTaskIndex = 0;
    }

    private SchedulerContext(final int lastTaskIndex) {
        assertPositive(lastTaskIndex);
        this.lastTaskIndex = lastTaskIndex;
    }

    public static FeedUpdateTaskSchedulerContext convert(final SchedulerContext context) {
        assertNotNull(context);

        return new FeedUpdateTaskSchedulerContext(context.lastTaskIndex);
    }

    public static SchedulerContext convert(final FeedUpdateTaskSchedulerContext context) {
        assertNotNull(context);

        return new SchedulerContext(context.lastTaskIndex);
    }

}
