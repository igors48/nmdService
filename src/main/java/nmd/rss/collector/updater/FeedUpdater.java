package nmd.rss.collector.updater;

import nmd.rss.collector.feed.*;
import nmd.rss.collector.scheduler.FeedUpdateTask;
import nmd.rss.collector.scheduler.FeedUpdateTaskScheduler;
import nmd.rss.collector.scheduler.FeedUpdateTaskSchedulerException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 01.05.13
 */
public final class FeedUpdater {

    private static final Logger LOGGER = Logger.getLogger(FeedUpdater.class.getName());

    public static void update(final FeedUpdateTaskScheduler scheduler, final FeedService feedService, final UrlFetcher fetcher) throws FeedUpdaterException {
        assertNotNull(scheduler);
        assertNotNull(feedService);
        assertNotNull(fetcher);

        try {
            final FeedUpdateTask task = scheduler.getCurrentTask();

            if (task == null) {
                LOGGER.info("There is no current task");

                return;
            }

            final FeedHeader header = feedService.loadHeader(task.feedId);

            if (header == null) {
                LOGGER.warning(String.format("Feed header for id [ %s ] not found", task.feedId));

                return;
            }

            final String feedData = fetcher.fetch(header.feedLink);
            final Feed parsedFeed = FeedParser.parse(header.feedLink, feedData);

            List<FeedItem> olds = feedService.loadItems(task.feedId);
            //TODO may be it is a responsibility of feed service ?
            olds = olds == null ? new ArrayList<FeedItem>() : olds;

            final FeedItemsMergeReport mergeReport = FeedItemsMerger.merge(olds, parsedFeed.items, task.maxFeedItemsCount);

            feedService.updateItems(task.feedId, mergeReport.removed, mergeReport.retained, mergeReport.added);

            LOGGER.info(String.format("Task completed. Feed id [ %s ] updated. Url [ %s ] Items removed [ %d ] retained [ %d ] added [ %d ]", task.feedId, header.link, mergeReport.removed.size(), mergeReport.retained.size(), mergeReport.added.size()));
        } catch (FeedUpdateTaskSchedulerException | FeedServiceException | UrlFetcherException | FeedParserException exception) {
            throw new FeedUpdaterException(exception);
        }
    }

    private FeedUpdater() {
        //empty
    }

}
