package nmd.rss.collector.controller;

import nmd.rss.collector.Transactions;
import nmd.rss.collector.feed.Feed;
import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.collector.feed.FeedParser;
import nmd.rss.collector.feed.FeedParserException;
import nmd.rss.collector.scheduler.FeedUpdateTask;
import nmd.rss.collector.scheduler.FeedUpdateTaskRepository;
import nmd.rss.collector.updater.*;

import javax.persistence.EntityTransaction;
import java.util.UUID;

import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.collector.util.Assert.assertStringIsValid;
import static nmd.rss.collector.util.TransactionTools.rollbackIfActive;

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
    private final Transactions transactions;
    private final FeedHeadersRepository feedHeadersRepository;
    private final FeedItemsRepository feedItemsRepository;
    private final FeedUpdateTaskRepository feedUpdateTaskRepository;
    private final UrlFetcher fetcher;

    public ControlService(final Transactions transactions, final FeedHeadersRepository feedHeadersRepository, final FeedItemsRepository feedItemsRepository, final FeedUpdateTaskRepository feedUpdateTaskRepository, final UrlFetcher fetcher) {
        assertNotNull(transactions);
        this.transactions = transactions;

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

        EntityTransaction transaction = null;

        try {
            transaction = this.transactions.getOne();
            transaction.begin();

            final String feedUrlInLowerCase = feedUrl.toLowerCase();
            final FeedHeader feedHeader = this.feedHeadersRepository.loadHeader(feedUrlInLowerCase);

            //TODO URL Fetch in transaction -- is not so good
            UUID feedId = feedHeader == null ? createNewFeed(feedUrlInLowerCase) : feedHeader.id;

            transaction.commit();

            return feedId;
        } catch (FeedServiceException | UrlFetcherException | FeedParserException exception) {
            throw new ControllerException(exception);
        } finally {
            rollbackIfActive(transaction);
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
        final UUID feedId = feed.header.id;
        final FeedUpdateTask feedUpdateTask = new FeedUpdateTask(UUID.randomUUID(), feedId);

        this.feedUpdateTaskRepository.storeTask(feedUpdateTask);
        this.feedHeadersRepository.storeHeader(feed.header);
        this.feedItemsRepository.updateItems(feedId, feed.items);

        return feedId;
    }

}
