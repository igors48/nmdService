package nmd.rss.collector.controller;

import nmd.rss.collector.error.ServiceException;
import nmd.rss.collector.feed.Feed;
import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.collector.feed.FeedItem;
import nmd.rss.collector.feed.FeedParserException;
import nmd.rss.collector.updater.FeedHeadersRepository;
import nmd.rss.collector.updater.FeedItemsRepository;
import nmd.rss.collector.updater.UrlFetcher;
import nmd.rss.collector.updater.UrlFetcherException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static nmd.rss.collector.error.ServiceError.*;
import static nmd.rss.collector.feed.FeedParser.parse;
import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 02.02.14
 */
public class AbstractService {

    protected final FeedHeadersRepository feedHeadersRepository;
    protected final FeedItemsRepository feedItemsRepository;
    protected final UrlFetcher fetcher;

    public AbstractService(final FeedHeadersRepository feedHeadersRepository, final FeedItemsRepository feedItemsRepository, /*final FeedUpdateTaskRepository feedUpdateTaskRepository, final ReadFeedItemsRepository readFeedItemsRepository, final FeedUpdateTaskSchedulerContextRepository feedUpdateTaskSchedulerContextRepository, final FeedUpdateTaskScheduler scheduler,*/ final UrlFetcher fetcher/*, final Transactions transactions*/) {
        assertNotNull(feedHeadersRepository);
        this.feedHeadersRepository = feedHeadersRepository;

        assertNotNull(feedItemsRepository);
        this.feedItemsRepository = feedItemsRepository;

        assertNotNull(fetcher);
        this.fetcher = fetcher;
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

            return parse(feedUrl, data);
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
