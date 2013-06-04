package nmd.rss.collector.updater;

import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.collector.scheduler.FeedUpdateTask;
import nmd.rss.collector.scheduler.FeedUpdateTaskRepository;

import java.util.*;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 09.05.13
 */
public class InMemoryFeedHeadersAndUpdateTasksRepository implements FeedHeadersRepository, FeedUpdateTaskRepository {

    private static final Map<UUID, FeedHeader> HEADERS;
    private static final List<FeedUpdateTask> TASKS;

    private static final UUID FIRST_HEADER_UUID = UUID.fromString("99dd645b-1a68-46b4-8b44-f7131386b105");
    private static final UUID SECOND_HEADER_UUID = UUID.fromString("58a50995-3918-4e3a-9998-e364e0515477");
    private static final UUID THIRD_HEADER_UUID = UUID.fromString("2f2ac46f-e6ea-4cb7-90d9-34a110a6e876");

    private static final UUID FIRST_TASK_UUID = UUID.fromString("d406ad12-ee61-4383-93cc-4a6c7b6e729b");
    private static final UUID SECOND_TASK_UUID = UUID.fromString("2723098d-df51-4c9b-b1b9-079d65c27ecf");
    private static final UUID THIRD_TASK_UUID = UUID.fromString("d9fa1dc2-bba0-43a6-bc85-b7554234591d");

    static {
        HEADERS = new HashMap<>();
        TASKS = new ArrayList<>();

        final FeedHeader firstHeader = new FeedHeader(FIRST_HEADER_UUID, "http://www.3dnews.ru/news/rss", "3DNews - Daily Digital Digest: Новости Hardware", "3DNews - Daily Digital Digest: Новости Hardware", "http://www.3dnews.ru/news/");
        final FeedHeader secondHeader = new FeedHeader(SECOND_HEADER_UUID, "http://bash.im/rss/", "Bash.im", "Цитатник Рунета", "http://bash.im/");
        final FeedHeader thirdHeader = new FeedHeader(THIRD_HEADER_UUID, "http://www.inopressa.ru/rss/", "Inopressa", "Обзор иностранной прессы", "http://www.inopressa.ru/");

        final FeedUpdateTask firstUpdateTask = new FeedUpdateTask(FIRST_TASK_UUID, FIRST_HEADER_UUID);
        final FeedUpdateTask secondUpdateTask = new FeedUpdateTask(SECOND_TASK_UUID, SECOND_HEADER_UUID);
        final FeedUpdateTask thirdUpdateTask = new FeedUpdateTask(THIRD_TASK_UUID, THIRD_HEADER_UUID);

        HEADERS.put(FIRST_HEADER_UUID, firstHeader);
        HEADERS.put(SECOND_HEADER_UUID, secondHeader);
        HEADERS.put(THIRD_HEADER_UUID, thirdHeader);

        TASKS.add(firstUpdateTask);
        TASKS.add(secondUpdateTask);
        TASKS.add(thirdUpdateTask);
    }

    @Override
    public FeedHeader loadHeader(final UUID feedId) {
        assertNotNull(feedId);

        return HEADERS.get(feedId);
    }

    @Override
    public FeedHeader loadHeader(String feedLink) {
        return null;
    }

    @Override
    public void storeHeader(final FeedHeader feedHeader) {
        assertNotNull(feedHeader);
    }

    @Override
    public List loadAllEntities() {
        return null;
    }

    @Override
    public void removeEntity(Object victim) {
    }

    @Override
    public List<FeedUpdateTask> loadAllTasks() {
        return TASKS;
    }

    @Override
    public void storeTask(final FeedUpdateTask feedUpdateTask) {
    }

}
