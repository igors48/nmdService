package nmd.rss.collector.scheduler;

import nmd.rss.collector.Transactions;

import javax.persistence.EntityTransaction;
import java.util.List;

import static nmd.rss.collector.scheduler.FeedUpdateTaskSchedulerContext.START_CONTEXT;
import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.collector.util.TransactionTools.rollbackIfActive;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 30.04.13
 */
public class CycleFeedUpdateTaskScheduler implements FeedUpdateTaskScheduler {

    private final Transactions transactions;
    private final FeedUpdateTaskSchedulerContextRepository contextRepository;
    private final FeedUpdateTaskRepository taskRepository;

    public CycleFeedUpdateTaskScheduler(final FeedUpdateTaskSchedulerContextRepository contextRepository, final FeedUpdateTaskRepository taskRepository, final Transactions transactions) {
        assertNotNull(transactions);
        this.transactions = transactions;

        assertNotNull(contextRepository);
        this.contextRepository = contextRepository;

        assertNotNull(taskRepository);
        this.taskRepository = taskRepository;
    }

    @Override
    public FeedUpdateTask getCurrentTask() throws FeedUpdateTaskSchedulerException {

        EntityTransaction transaction = null;

        try {
            transaction = this.transactions.getOne();
            transaction.begin();

            FeedUpdateTaskSchedulerContext context = this.contextRepository.load();
            context = context == null ? START_CONTEXT : context;

            final List<FeedUpdateTask> tasks = this.taskRepository.loadAllTasks();

            if (tasks.isEmpty()) {
                return null;
            }

            int taskIndex = context.lastTaskIndex;
            taskIndex = taskIndex > tasks.size() - 1 ? 0 : taskIndex;

            final FeedUpdateTaskSchedulerContext newContext = new FeedUpdateTaskSchedulerContext(taskIndex + 1);
            this.contextRepository.store(newContext);

            transaction.commit();

            return tasks.get(taskIndex);
        } catch (Exception exception) {
            throw new FeedUpdateTaskSchedulerException(exception);
        } finally {
            rollbackIfActive(transaction);
        }
    }

}
