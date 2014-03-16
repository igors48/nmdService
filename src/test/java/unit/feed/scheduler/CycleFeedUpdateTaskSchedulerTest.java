package unit.feed.scheduler;

import nmd.rss.collector.scheduler.CycleFeedUpdateTaskScheduler;
import nmd.rss.collector.scheduler.FeedUpdateTask;
import nmd.rss.collector.scheduler.FeedUpdateTaskSchedulerContext;
import nmd.rss.collector.scheduler.FeedUpdateTaskSchedulerException;
import org.junit.Before;
import org.junit.Test;
import unit.feed.controller.stub.TransactionsStub;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 30.04.13
 */
public class CycleFeedUpdateTaskSchedulerTest {

    private static final int MAX_FEED_ITEMS_COUNT = 10;

    private static final FeedUpdateTask FIRST_TASK = new FeedUpdateTask(UUID.randomUUID(), MAX_FEED_ITEMS_COUNT);
    private static final FeedUpdateTask SECOND_TASK = new FeedUpdateTask(UUID.randomUUID(), MAX_FEED_ITEMS_COUNT);
    private static final FeedUpdateTask THIRD_TASK = new FeedUpdateTask(UUID.randomUUID(), MAX_FEED_ITEMS_COUNT);

    private FeedUpdateTaskRepositoryStub taskRepositoryStub;
    private FeedUpdateTaskSchedulerContextRepositoryStub contextRepositoryStub;
    private CycleFeedUpdateTaskScheduler scheduler;

    @Before
    public void before() {
        this.taskRepositoryStub = new FeedUpdateTaskRepositoryStub(FIRST_TASK, SECOND_TASK);
        this.contextRepositoryStub = new FeedUpdateTaskSchedulerContextRepositoryStub();

        this.scheduler = new CycleFeedUpdateTaskScheduler(this.contextRepositoryStub, this.taskRepositoryStub, new TransactionsStub());
    }

    @Test
    public void whenNoContextStoredInRepositoryThenUseStartContext() throws FeedUpdateTaskSchedulerException {
        FeedUpdateTask updateTask = this.scheduler.getCurrentTask();
        assertEquals(FIRST_TASK, updateTask);
    }

    @Test
    public void schedulerUpdatesItsContext() throws FeedUpdateTaskSchedulerException {
        this.scheduler.getCurrentTask();

        FeedUpdateTaskSchedulerContext context = this.contextRepositoryStub.load();

        assertEquals(1, context.lastTaskIndex);
    }

    @Test
    public void schedulerSelectsTasksCyclically() throws FeedUpdateTaskSchedulerException {
        FeedUpdateTask first = this.scheduler.getCurrentTask();
        FeedUpdateTask second = this.scheduler.getCurrentTask();
        FeedUpdateTask third = this.scheduler.getCurrentTask();

        assertEquals(FIRST_TASK, first);
        assertEquals(SECOND_TASK, second);
        assertEquals(FIRST_TASK, third);
    }

    @Test
    public void afterTaskAddedItExecuted() throws FeedUpdateTaskSchedulerException {
        this.scheduler.getCurrentTask();
        this.scheduler.getCurrentTask();

        this.taskRepositoryStub.storeTask(THIRD_TASK);

        FeedUpdateTask updateTask = this.scheduler.getCurrentTask();

        assertEquals(THIRD_TASK, updateTask);
    }

    @Test
    public void afterTaskRemovedItDidNotExecute() throws FeedUpdateTaskSchedulerException {
        this.scheduler.getCurrentTask();
        this.scheduler.getCurrentTask();

        this.taskRepositoryStub.deleteTaskForFeedId(SECOND_TASK.feedId);

        FeedUpdateTask updateTask = this.scheduler.getCurrentTask();

        assertEquals(FIRST_TASK, updateTask);
    }

    @Test
    public void ifThereIsNoTasksThenNullReturns() throws FeedUpdateTaskSchedulerException {
        this.taskRepositoryStub.deleteTaskForFeedId(FIRST_TASK.feedId);
        this.taskRepositoryStub.deleteTaskForFeedId(SECOND_TASK.feedId);

        FeedUpdateTask updateTask = this.scheduler.getCurrentTask();
        assertNull(updateTask);
    }

}
