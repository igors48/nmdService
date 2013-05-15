package feed.gae;

import nmd.rss.collector.gae.updater.SchedulerContext;
import nmd.rss.collector.scheduler.FeedUpdateTaskSchedulerContext;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 15.05.13
 */
public class FeedUpdateTaskSchedulerContextConversionTest {

    @Test
    public void roundtrip() {
        FeedUpdateTaskSchedulerContext initial = new FeedUpdateTaskSchedulerContext(48);
        SchedulerContext converted = SchedulerContext.convert(initial);
        FeedUpdateTaskSchedulerContext restored = SchedulerContext.convert(converted);

        assertEquals(initial, restored);
    }

}
