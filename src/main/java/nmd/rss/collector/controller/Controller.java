package nmd.rss.collector.controller;

import nmd.rss.collector.feed.Feed;
import nmd.rss.collector.feed.FeedParser;
import nmd.rss.collector.feed.FeedParserException;
import nmd.rss.collector.updater.FeedService;
import nmd.rss.collector.updater.FeedServiceException;
import nmd.rss.collector.updater.UrlFetcher;
import nmd.rss.collector.updater.UrlFetcherException;

import java.util.UUID;

import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.collector.util.Assert.assertStringIsValid;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.05.13
 */
public class Controller {

    private final FeedService feedService;
    private final UrlFetcher fetcher;

    public Controller(final FeedService feedService, final UrlFetcher fetcher) {
        assertNotNull(feedService);
        this.feedService = feedService;

        assertNotNull(fetcher);
        this.fetcher = fetcher;
    }

    public UUID addFeed(final String feedUrl) throws ControllerException {
        assertStringIsValid(feedUrl);

        try {
            final String feedUrlInLowerCase = feedUrl.toLowerCase();
            final UUID feedId = this.feedService.findForLink(feedUrlInLowerCase);

            return feedId == null ? createNewFeed(feedUrlInLowerCase) : feedId;
        } catch (FeedServiceException | UrlFetcherException | FeedParserException exception) {
            throw new ControllerException(exception);
        }
    }

    public boolean removeFeed(final UUID feedId) throws ControllerException {
        assertNotNull(feedId);

        try {
            return this.feedService.removeFeed(feedId);
        } catch (FeedServiceException exception) {
            throw new ControllerException(exception);
        }
    }

    private UUID createNewFeed(final String feedUrl) throws UrlFetcherException, FeedParserException, FeedServiceException {
        final String data = this.fetcher.fetch(feedUrl);
        final Feed feed = FeedParser.parse(feedUrl, data);

        return this.feedService.addFeed(feed);
    }

}
