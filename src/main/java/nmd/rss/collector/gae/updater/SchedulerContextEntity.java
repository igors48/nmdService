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
@Entity(name = SchedulerContextEntity.NAME)
public class SchedulerContextEntity {

    public static final String NAME = "SchedulerContext";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private com.google.appengine.api.datastore.Key key;

    private int lastTaskIndex;

    private SchedulerContextEntity() {
        this.lastTaskIndex = 0;
    }

    private SchedulerContextEntity(final int lastTaskIndex) {
        assertPositive(lastTaskIndex);
        this.lastTaskIndex = lastTaskIndex;
    }

    public static FeedUpdateTaskSchedulerContext convert(final SchedulerContextEntity context) {
        assertNotNull(context);

        return new FeedUpdateTaskSchedulerContext(context.lastTaskIndex);
    }

    public static SchedulerContextEntity convert(final FeedUpdateTaskSchedulerContext context) {
        assertNotNull(context);

        return new SchedulerContextEntity(context.lastTaskIndex);
    }

}
