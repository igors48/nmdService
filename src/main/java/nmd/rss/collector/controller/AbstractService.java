package nmd.rss.collector.controller;

import nmd.rss.collector.error.ServiceException;
import nmd.rss.collector.feed.Feed;
import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.collector.feed.FeedItem;
import nmd.rss.collector.feed.FeedParserException;
import nmd.rss.collector.twitter.TwitterClient;
import nmd.rss.collector.twitter.entities.Tweet;
import nmd.rss.collector.updater.FeedHeadersRepository;
import nmd.rss.collector.updater.FeedItemsRepository;
import nmd.rss.collector.updater.UrlFetcher;
import nmd.rss.collector.updater.UrlFetcherException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static nmd.rss.collector.error.ServiceError.*;
import static nmd.rss.collector.feed.FeedParser.parse;
import static nmd.rss.collector.twitter.TweetConversionTools.convertToFeed;
import static nmd.rss.collector.twitter.TwitterClientTools.getTwitterUserName;
import static nmd.rss.collector.twitter.TwitterClientTools.isItTwitterUrl;
import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 02.02.14
 */
public class AbstractService {

    protected final FeedHeadersRepository feedHeadersRepository;
    protected final FeedItemsRepository feedItemsRepository;
    protected final UrlFetcher fetcher;

    public AbstractService(final FeedHeadersRepository feedHeadersRepository, final FeedItemsRepository feedItemsRepository, final UrlFetcher fetcher) {
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
            final boolean isItTwitterUrl = isItTwitterUrl(feedUrl);

            return isItTwitterUrl ? fetchAsTwitterUrl(feedUrl) : fetchAsCommonUrl(feedUrl);
        } catch (final UrlFetcherException exception) {
            throw new ServiceException(urlFetcherError(feedUrl), exception);
        } catch (FeedParserException exception) {
            throw new ServiceException(feedParseError(feedUrl), exception);
        }
    }

    private Feed fetchAsTwitterUrl(final String twitterUrl) throws ServiceException {

        try {
            //TODO move apiKey, apiSecret to configuration
            final TwitterClient twitterClient = new TwitterClient("tjOc6yZT0a0QzxOLEpqGg", "avqQAxuOVlpHm09YsukVxfdIBAlhjVRqbWmVzJ1yVgs");
            final String userName = getTwitterUserName(twitterUrl);
            final List<Tweet> tweets = twitterClient.fetchTweets(userName, 1000);
            final Feed feed = convertToFeed(twitterUrl, tweets, new Date());

            if (feed == null) {
                throw new ServiceException(feedParseError(twitterUrl));
            }

            return feed;
        } catch (IOException exception) {
            throw new ServiceException(urlFetcherError(twitterUrl), exception);
        }
    }

    private Feed fetchAsCommonUrl(String feedUrl) throws UrlFetcherException, FeedParserException {
        final String data = this.fetcher.fetch(feedUrl);

        return parse(feedUrl, data);
    }

    protected List<FeedItem> getFeedOldItems(final FeedHeader feedHeader) {
        final List<FeedItem> feedItems = feedHeader == null ? new ArrayList<FeedItem>() : this.feedItemsRepository.loadItems(feedHeader.id);

        return feedItems == null ? new ArrayList<FeedItem>() : feedItems;
    }

}
