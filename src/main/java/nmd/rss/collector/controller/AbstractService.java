package nmd.rss.collector.controller;

import nmd.rss.collector.Transactions;
import nmd.rss.collector.error.ServiceException;
import nmd.rss.collector.feed.*;
import nmd.rss.collector.scheduler.FeedUpdateTaskRepository;
import nmd.rss.collector.scheduler.FeedUpdateTaskScheduler;
import nmd.rss.collector.scheduler.FeedUpdateTaskSchedulerContextRepository;
import nmd.rss.collector.updater.FeedHeadersRepository;
import nmd.rss.collector.updater.FeedItemsRepository;
import nmd.rss.collector.updater.UrlFetcher;
import nmd.rss.collector.updater.UrlFetcherException;
import nmd.rss.reader.ReadFeedItemsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static nmd.rss.collector.error.ServiceError.*;
import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 02.02.14
 */
public class AbstractService {

    protected static final int MAX_FEED_ITEMS_COUNT = 300;

    protected final Transactions transactions;
    protected final FeedHeadersRepository feedHeadersRepository;
    protected final FeedItemsRepository feedItemsRepository;
    protected final FeedUpdateTaskRepository feedUpdateTaskRepository;
    protected final ReadFeedItemsRepository readFeedItemsRepository;
    protected final FeedUpdateTaskSchedulerContextRepository feedUpdateTaskSchedulerContextRepository;
    protected final FeedUpdateTaskScheduler scheduler;
    protected final UrlFetcher fetcher;

    public AbstractService(final FeedHeadersRepository feedHeadersRepository, final FeedItemsRepository feedItemsRepository, final FeedUpdateTaskRepository feedUpdateTaskRepository, final ReadFeedItemsRepository readFeedItemsRepository, final FeedUpdateTaskSchedulerContextRepository feedUpdateTaskSchedulerContextRepository, final FeedUpdateTaskScheduler scheduler, final UrlFetcher fetcher, final Transactions transactions) {
        assertNotNull(feedHeadersRepository);
        this.feedHeadersRepository = feedHeadersRepository;

        assertNotNull(feedItemsRepository);
        this.feedItemsRepository = feedItemsRepository;

        assertNotNull(feedUpdateTaskRepository);
        this.feedUpdateTaskRepository = feedUpdateTaskRepository;

        assertNotNull(readFeedItemsRepository);
        this.readFeedItemsRepository = readFeedItemsRepository;

        assertNotNull(feedUpdateTaskSchedulerContextRepository);
        this.feedUpdateTaskSchedulerContextRepository = feedUpdateTaskSchedulerContextRepository;

        assertNotNull(scheduler);
        this.scheduler = scheduler;

        assertNotNull(fetcher);
        this.fetcher = fetcher;

        assertNotNull(transactions);
        this.transactions = transactions;
    }

    public FeedHeader loadFeedHeader(final UUID feedId) throws ServiceException {
        FeedHeader header = this.feedHeadersRepository.loadHeader(feedId);

        if (header == null) {
            throw new ServiceException(wrongFeedId(feedId));
        }

        return header;
    }

    protected Feed fetchFeed(final String feedUrl) throws ServiceException {

        try {
            final String data = this.fetcher.fetch(feedUrl);

            return FeedParser.parse(feedUrl, data);
        } catch (final UrlFetcherException exception) {
            throw new ServiceException(urlFetcherError(feedUrl), exception);
        } catch (FeedParserException exception) {
            throw new ServiceException(feedParseError(feedUrl), exception);
        }
    }

    protected List<FeedItem> getFeedOldItems(final FeedHeader feedHeader) {
        final List<FeedItem> feedItems = feedHeader == null ? new ArrayList<FeedItem>() : this.feedItemsRepository.loadItems(feedHeader.id);

        return feedItems == null ? new ArrayList<FeedItem>() : feedItems;
    }

}
