package nmd.rss.collector.controller;

import nmd.rss.collector.error.ServiceException;
import nmd.rss.collector.feed.Feed;
import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.collector.feed.FeedItem;
import nmd.rss.collector.updater.FeedHeadersRepository;
import nmd.rss.collector.updater.FeedItemsRepository;
import nmd.rss.collector.updater.UrlFetcher;
import nmd.rss.collector.updater.UrlFetcherException;
import nmd.rss.sources.rss.FeedParserException;
import nmd.rss.sources.twitter.TwitterClient;
import nmd.rss.sources.twitter.entities.Tweet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static nmd.rss.collector.error.ServiceError.*;
import static nmd.rss.collector.feed.FeedHeader.isValidFeedHeaderId;
import static nmd.rss.collector.util.Assert.guard;
import static nmd.rss.collector.util.Parameter.notNull;
import static nmd.rss.sources.rss.FeedParser.parse;
import static nmd.rss.sources.twitter.TweetConversionTools.convertToFeed;
import static nmd.rss.sources.twitter.TwitterClientTools.getTwitterUserName;
import static nmd.rss.sources.twitter.TwitterClientTools.isItTwitterUrl;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 02.02.14
 */
public class AbstractService {

    protected final FeedHeadersRepository feedHeadersRepository;
    protected final FeedItemsRepository feedItemsRepository;
    protected final UrlFetcher fetcher;

    public AbstractService(final FeedHeadersRepository feedHeadersRepository, final FeedItemsRepository feedItemsRepository, final UrlFetcher fetcher) {
        guard(notNull(feedHeadersRepository));
        this.feedHeadersRepository = feedHeadersRepository;

        guard(notNull(feedItemsRepository));
        this.feedItemsRepository = feedItemsRepository;

        guard(notNull(fetcher));
        this.fetcher = fetcher;
    }

    public FeedHeader loadFeedHeader(final UUID feedId) throws ServiceException {
        guard(isValidFeedHeaderId(feedId));

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

    protected List<FeedItem> getFeedOldItems(final FeedHeader feedHeader) {
        final List<FeedItem> feedItems = feedHeader == null ? new ArrayList<FeedItem>() : this.feedItemsRepository.loadItems(feedHeader.id);

        return feedItems == null ? new ArrayList<FeedItem>() : feedItems;
    }

    private Feed fetchAsTwitterUrl(final String twitterUrl) throws ServiceException {

        try {
            final String apiKey = System.getProperty("twitter.apiKey");
            final String apiSecret = System.getProperty("twitter.apiSecret");

            final TwitterClient twitterClient = new TwitterClient(apiKey, apiSecret);
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

    private Feed fetchAsCommonUrl(final String feedUrl) throws UrlFetcherException, FeedParserException {
        final String data = this.fetcher.fetch(feedUrl);

        return parse(feedUrl, data);
    }

}
