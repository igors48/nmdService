package nmd.rss.collector.gae.task;

import com.google.appengine.api.datastore.Key;
import nmd.rss.collector.scheduler.FeedUpdateTask;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.UUID;

import static nmd.rss.collector.util.Assert.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 31.05.13
 */
@Entity(name = FeedUpdateTaskEntity.NAME)
public class FeedUpdateTaskEntity {

    public static final String NAME = "FeedUpdateTask";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;

    private String feedId;
    private int maxFeedItemsCount;

    private FeedUpdateTaskEntity() {
        // empty
    }

    private FeedUpdateTaskEntity(final String feedId, final int maxFeedItemsCount) {
        assertStringIsValid(feedId);
        this.feedId = feedId;

        assertPositive(maxFeedItemsCount);
        this.maxFeedItemsCount = maxFeedItemsCount;
    }

    public static FeedUpdateTaskEntity convert(final FeedUpdateTask feedUpdateTask) {
        assertNotNull(feedUpdateTask);

        return new FeedUpdateTaskEntity(feedUpdateTask.feedId.toString(), feedUpdateTask.maxFeedItemsCount);
    }

    public static FeedUpdateTask convert(final FeedUpdateTaskEntity feedUpdateTaskEntity) {
        assertNotNull(feedUpdateTaskEntity);

        return new FeedUpdateTask(UUID.fromString(feedUpdateTaskEntity.feedId), feedUpdateTaskEntity.maxFeedItemsCount);
    }

}
