package nmd.rss.collector.updater;

import nmd.rss.collector.feed.*;
import nmd.rss.collector.scheduler.FeedUpdateTask;
import nmd.rss.collector.scheduler.FeedUpdateTaskScheduler;
import nmd.rss.collector.scheduler.FeedUpdateTaskSchedulerException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.collector.util.Assert.assertPositive;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 01.05.13
 */
public class FeedUpdater {

    private static final Logger LOGGER = Logger.getLogger(FeedUpdater.class.getName());

    public static void update(final FeedUpdateTaskScheduler scheduler, final FeedService feedService, final UrlFetcher fetcher, final int maxItemsCount) throws FeedUpdaterException {
        assertNotNull(scheduler);
        assertNotNull(feedService);
        assertNotNull(fetcher);
        assertPositive(maxItemsCount);

        try {
            FeedUpdateTask task = scheduler.getCurrentTask();

            if (task == null) {
                LOGGER.info("There is no current task");

                return;
            }

            FeedHeader header = feedService.loadHeader(task.feedId);

            if (header == null) {
                LOGGER.warning(String.format("Feed header for id [ %s ] not found", task.feedId));

                return;
            }

            String feedData = fetcher.fetch(header.link);
            Feed parsedFeed = FeedParser.parse(feedData);

            List<FeedItem> olds = feedService.loadItems(task.feedId);
            //TODO may be it is a responsibility of feed service ?
            olds = olds == null ? new ArrayList<FeedItem>() : olds;

            FeedItemsMergeReport mergeReport = FeedItemsMerger.merge(olds, parsedFeed.items, maxItemsCount);

            feedService.updateItems(task.feedId, mergeReport.removed, mergeReport.retained, mergeReport.added);
        } catch (FeedUpdateTaskSchedulerException | FeedServiceException | UrlFetcherException | FeedParserException exception) {
            throw new FeedUpdaterException(exception);
        }
    }

}
