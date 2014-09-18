package nmd.orb.services;

import nmd.orb.collector.fetcher.UrlFetcher;
import nmd.orb.collector.fetcher.UrlFetcherException;
import nmd.orb.error.ServiceException;
import nmd.orb.feed.Feed;
import nmd.orb.feed.FeedHeader;
import nmd.orb.feed.FeedItem;
import nmd.orb.repositories.FeedHeadersRepository;
import nmd.orb.repositories.FeedItemsRepository;
import nmd.orb.sources.rss.FeedParserException;
import nmd.orb.sources.twitter.TwitterClient;
import nmd.orb.sources.twitter.entities.Tweet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static nmd.orb.error.ServiceError.*;
import static nmd.orb.feed.FeedHeader.isValidFeedHeaderId;
import static nmd.orb.sources.rss.FeedParser.parse;
import static nmd.orb.sources.twitter.TweetConversionTools.convertToFeed;
import static nmd.orb.sources.twitter.TwitterClientTools.getTwitterUserName;
import static nmd.orb.sources.twitter.TwitterClientTools.isItTwitterUrl;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 02.02.14
 */
public class AbstractService {

    private static final String TWITTER_API_KEY = "twitter.apiKey";
    private static final String TWITTER_API_SECRET = "twitter.apiSecret";
    private static final int TWEETS_PER_FETCH = 100;

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
            final String apiKey = System.getProperty(TWITTER_API_KEY);
            final String apiSecret = System.getProperty(TWITTER_API_SECRET);

            final TwitterClient twitterClient = new TwitterClient(apiKey, apiSecret);
            final String userName = getTwitterUserName(twitterUrl);
            final List<Tweet> tweets = twitterClient.fetchTweets(userName, TWEETS_PER_FETCH);
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
