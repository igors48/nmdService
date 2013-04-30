package feed.scheduler;

import nmd.rss.collector.scheduler.FeedUpdateTaskScheduler;
import org.junit.Test;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 30.04.13
 */
public class FeedUpdateTaskSchedulerTest {

    @Test
    public void whenNoContextStoredInRepositoryThenUseStartContext() throws Exception {
        FeedUpdateTaskRepositoryStub taskRepositoryStub = new FeedUpdateTaskRepositoryStub();
        FeedUpdateTaskSchedulerContextRepositoryStub contextRepositoryStub = new FeedUpdateTaskSchedulerContextRepositoryStub();
        FeedUpdateTaskScheduler scheduler = new FeedUpdateTaskScheduler(contextRepositoryStub, taskRepositoryStub);


    }

    @Test
    public void schedulerUpdatesItsContext() {


    }

    //cyclic behaviour
    //behaviour when task added
    //behaviour when task deleted
}
