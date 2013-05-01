package nmd.rss.collector.updater;

import nmd.rss.collector.feed.*;
import nmd.rss.collector.scheduler.FeedUpdateTask;
import nmd.rss.collector.scheduler.FeedUpdateTaskScheduler;
import nmd.rss.collector.scheduler.FeedUpdateTaskSchedulerException;

import java.util.List;

import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.collector.util.Assert.assertPositive;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 01.05.13
 */
public class FeedUpdater {

    public static void update(final FeedUpdateTaskScheduler scheduler, final FeedService feedService, final UrlFetcher fetcher, final int maxItemsCount) throws FeedUpdaterException {
        assertNotNull(scheduler);
        assertNotNull(feedService);
        assertNotNull(fetcher);
        assertPositive(maxItemsCount);

        try {
            FeedUpdateTask task = scheduler.getCurrentTask();
            FeedHeader header = feedService.loadHeader(task.feedId);

            String feedData = fetcher.fetch(header.link);
            Feed parsedFeed = FeedParser.parse(feedData);

            List<FeedItem> olds = feedService.loadItems(task.feedId);

            FeedItemsMergeReport mergeReport = FeedItemsMerger.merge(olds, parsedFeed.items, maxItemsCount);
        } catch (FeedUpdateTaskSchedulerException | FeedServiceException | UrlFetcherException | FeedParserException exception) {
            throw new FeedUpdaterException(exception);
        }
    }

}
