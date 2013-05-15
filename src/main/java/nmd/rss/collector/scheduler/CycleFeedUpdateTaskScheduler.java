package nmd.rss.collector.scheduler;

import java.util.List;

import static nmd.rss.collector.scheduler.FeedUpdateTaskSchedulerContext.START_CONTEXT;
import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 30.04.13
 */
public class CycleFeedUpdateTaskScheduler implements FeedUpdateTaskScheduler {

    private final FeedUpdateTaskSchedulerContextService contextService;
    private final FeedUpdateTaskRepository taskRepository;

    public CycleFeedUpdateTaskScheduler(final FeedUpdateTaskSchedulerContextService contextService, final FeedUpdateTaskRepository taskRepository) {
        assertNotNull(contextService);
        this.contextService = contextService;

        assertNotNull(taskRepository);
        this.taskRepository = taskRepository;
    }

    @Override
    public FeedUpdateTask getCurrentTask() throws FeedUpdateTaskSchedulerException {

        try {
            FeedUpdateTaskSchedulerContext context = this.contextService.load();
            context = context == null ? START_CONTEXT : context;

            final List<FeedUpdateTask> tasks = this.taskRepository.loadAllTasks();

            if (tasks.isEmpty()) {
                return null;
            }

            int taskIndex = context.lastTaskIndex;
            taskIndex = taskIndex > tasks.size() - 1 ? 0 : taskIndex;

            final FeedUpdateTaskSchedulerContext newContext = new FeedUpdateTaskSchedulerContext(taskIndex + 1);
            this.contextService.store(newContext);

            return tasks.get(taskIndex);
        } catch (Exception exception) {
            throw new FeedUpdateTaskSchedulerException(exception);
        }
    }

}
