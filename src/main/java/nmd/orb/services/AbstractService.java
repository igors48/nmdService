package nmd.orb.services;

import com.sun.syndication.io.FeedException;
import nmd.orb.collector.fetcher.UrlFetcher;
import nmd.orb.collector.fetcher.UrlFetcherException;
import nmd.orb.error.ServiceException;
import nmd.orb.feed.Feed;
import nmd.orb.feed.FeedHeader;
import nmd.orb.feed.FeedItem;
import nmd.orb.repositories.FeedHeadersRepository;
import nmd.orb.repositories.FeedItemsRepository;
import nmd.orb.sources.Source;
import nmd.orb.sources.instagram.InstagramClient;
import nmd.orb.sources.rss.RssClient;
import nmd.orb.sources.twitter.TwitterClient;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static nmd.orb.error.ServiceError.*;
import static nmd.orb.feed.FeedHeader.isValidFeedHeaderId;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

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
            final Source source = Source.detect(feedUrl);

            switch (source) {
                case TWITTER:
                    return TwitterClient.fetchAsTwitterUrl(feedUrl);
                case INSTAGRAM:
                    return InstagramClient.fetchAsInstagramUrl(feedUrl);
                default:
                    return RssClient.fetchAsRssUrl(feedUrl, this.fetcher);
            }
        } catch (UrlFetcherException exception) {
            throw new ServiceException(urlFetcherError(feedUrl), exception);
        } catch (FeedException | UnsupportedEncodingException exception) {
            throw new ServiceException(feedParseError(feedUrl), exception);
        }
    }

    protected List<FeedItem> getFeedOldItems(final FeedHeader feedHeader) {
        final List<FeedItem> feedItems = feedHeader == null ? new ArrayList<FeedItem>() : this.feedItemsRepository.loadItems(feedHeader.id);

        return feedItems == null ? new ArrayList<FeedItem>() : feedItems;
    }

}
