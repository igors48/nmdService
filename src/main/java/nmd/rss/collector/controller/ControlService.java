package nmd.rss.collector.controller;

import nmd.rss.collector.feed.Feed;
import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.collector.feed.FeedParser;
import nmd.rss.collector.feed.FeedParserException;
import nmd.rss.collector.scheduler.FeedUpdateTaskRepository;
import nmd.rss.collector.updater.*;

import javax.persistence.EntityManager;
import java.util.UUID;

import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.collector.util.Assert.assertStringIsValid;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.05.13
 */
public class ControlService {

    /*
    UUID findForLink(String link) throws FeedServiceException;

    UUID addFeed(Feed feed) throws FeedServiceException;

    boolean removeFeed(UUID feedId) throws FeedServiceException;
     */

    //TODO append task to scheduler after feed added
    //TODO remove task from scheduler after feed deleted

    //private final FeedService feedService;
    private final FeedHeadersRepository feedHeadersRepository;
    private final FeedItemsRepository feedItemsRepository;
    private final FeedUpdateTaskRepository feedUpdateTaskRepository;
    private final UrlFetcher fetcher;

    public ControlService(final EntityManager entityManager, final FeedHeadersRepository feedHeadersRepository, final FeedItemsRepository feedItemsRepository, final FeedUpdateTaskRepository feedUpdateTaskRepository, final UrlFetcher fetcher) {
        assertNotNull(feedHeadersRepository);
        this.feedHeadersRepository = feedHeadersRepository;

        assertNotNull(feedItemsRepository);
        this.feedItemsRepository = feedItemsRepository;

        assertNotNull(feedUpdateTaskRepository);
        this.feedUpdateTaskRepository = feedUpdateTaskRepository;

        assertNotNull(fetcher);
        this.fetcher = fetcher;
    }

    public UUID addFeed(final String feedUrl) throws ControllerException {
        assertStringIsValid(feedUrl);

        try {
            final String feedUrlInLowerCase = feedUrl.toLowerCase();
            final FeedHeader feedHeader = this.feedHeadersRepository.loadHeader(feedUrlInLowerCase);

            return feedHeader == null ? createNewFeed(feedUrlInLowerCase) : feedHeader.id;
        } catch (FeedServiceException | UrlFetcherException | FeedParserException exception) {
            throw new ControllerException(exception);
        }
    }

    public boolean removeFeed(final UUID feedId) throws ControllerException {
        assertNotNull(feedId);

        return false;
        /*
        try {
            return this.feedService.removeFeed(feedId);
        } catch (FeedServiceException exception) {
            throw new ControllerException(exception);
        }
        */
    }

    private UUID createNewFeed(final String feedUrl) throws UrlFetcherException, FeedParserException, FeedServiceException {
        final String data = this.fetcher.fetch(feedUrl);
        final Feed feed = FeedParser.parse(feedUrl, data);

        return null;//this.feedService.addFeed(feed);
    }

}
