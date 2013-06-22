package nmd.rss.collector.rest;

import nmd.rss.collector.EntityManagerTransactions;
import nmd.rss.collector.Transactions;
import nmd.rss.collector.controller.ControlService;
import nmd.rss.collector.gae.feed.GaeFeedHeadersRepository;
import nmd.rss.collector.gae.feed.GaeFeedItemsRepository;
import nmd.rss.collector.gae.fetcher.GaeUrlFetcher;
import nmd.rss.collector.gae.task.GaeFeedUpdateTaskRepository;
import nmd.rss.collector.scheduler.FeedUpdateTaskRepository;
import nmd.rss.collector.updater.FeedHeadersRepository;
import nmd.rss.collector.updater.FeedItemsRepository;
import nmd.rss.collector.updater.UrlFetcher;

import javax.persistence.EntityManager;
import java.util.UUID;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.06.13
 */
public class ControlServiceWrapper {

    public static String addFeed(final String feedUrl) {
        return null;
    }

    public static String removeFeed(final UUID feedId) {
        return null;
    }

    public static String getFeedHeaders() {
        return null;
    }

    public static String getFeed(final UUID feedId) {
        return null;
    }

    private static ControlService createControlService(final EntityManager entityManager) {
        final Transactions transactions = new EntityManagerTransactions(entityManager);
        final UrlFetcher urlFetcher = new GaeUrlFetcher();
        final FeedUpdateTaskRepository feedUpdateTaskRepository = new GaeFeedUpdateTaskRepository(entityManager);
        final FeedItemsRepository feedItemsRepository = new GaeFeedItemsRepository(entityManager);
        final FeedHeadersRepository feedHeadersRepository = new GaeFeedHeadersRepository(entityManager);

        return new ControlService(feedHeadersRepository, feedItemsRepository, feedUpdateTaskRepository, urlFetcher, transactions);
    }

}
