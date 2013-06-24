package nmd.rss.collector.rest;

import com.google.gson.Gson;
import nmd.rss.collector.EntityManagerTransactions;
import nmd.rss.collector.Transactions;
import nmd.rss.collector.controller.ControlService;
import nmd.rss.collector.controller.ControlServiceException;
import nmd.rss.collector.error.ServiceError;
import nmd.rss.collector.exporter.FeedExporter;
import nmd.rss.collector.exporter.FeedExporterException;
import nmd.rss.collector.feed.Feed;
import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.collector.gae.feed.GaeFeedHeadersRepository;
import nmd.rss.collector.gae.feed.GaeFeedItemsRepository;
import nmd.rss.collector.gae.fetcher.GaeUrlFetcher;
import nmd.rss.collector.gae.task.GaeFeedUpdateTaskRepository;
import nmd.rss.collector.scheduler.FeedUpdateTaskRepository;
import nmd.rss.collector.updater.FeedHeadersRepository;
import nmd.rss.collector.updater.FeedItemsRepository;
import nmd.rss.collector.updater.UrlFetcher;
import persistense.EMF;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.UUID;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.06.13
 */
public class ControlServiceWrapper {

    private static final Gson GSON = new Gson();

    public static ResponseBody addFeed(final String feedUrl) {
        //TODO feedUrl can be null. need to check it
        final EntityManager entityManager = EMF.get().createEntityManager();
        final ControlService controlService = createControlService(entityManager);

        try {
            final UUID feedId = controlService.addFeed(feedUrl);

            final FeedIdResponse feedIdResponse = new FeedIdResponse(feedId);

            return createJsonResponse(feedIdResponse);
        } catch (ControlServiceException exception) {
            return createErrorJsonResponse(exception);
        } finally {
            entityManager.close();
        }
    }

    public static ResponseBody removeFeed(final UUID feedId) {
        //TODO feedId can be null. need to check it
        final EntityManager entityManager = EMF.get().createEntityManager();
        final ControlService controlService = createControlService(entityManager);

        try {
            controlService.removeFeed(feedId);

            final SuccessMessageResponse successMessageResponse = new SuccessMessageResponse(String.format("Feed [ %s ] removed", feedId));

            return createJsonResponse(successMessageResponse);
        } finally {
            entityManager.close();
        }
    }

    public static ResponseBody getFeedHeaders() {
        final EntityManager entityManager = EMF.get().createEntityManager();
        final ControlService controlService = createControlService(entityManager);

        try {
            final List<FeedHeader> headers = controlService.getFeedHeaders();
            final FeedHeadersResponse feedHeadersResponse = FeedHeadersResponse.convert(headers);

            return createJsonResponse(feedHeadersResponse);
        } finally {
            entityManager.close();
        }
    }

    public static ResponseBody getFeed(final UUID feedId) {
        //TODO feedId can be null. need to check it
        final EntityManager entityManager = EMF.get().createEntityManager();
        final ControlService controlService = createControlService(entityManager);

        try {
            final Feed feed = controlService.getFeed(feedId);
            final String feedAsXml = FeedExporter.export(feed.header, feed.items);

            return new ResponseBody(ContentType.XML, feedAsXml);
        } catch (ControlServiceException exception) {
            return createErrorJsonResponse(exception);
        } catch (FeedExporterException exception) {
            return createErrorJsonResponse(ServiceError.feedExportError(feedId));
        } finally {
            entityManager.close();
        }
    }

    private static ResponseBody createJsonResponse(final Object object) {
        final String content = GSON.toJson(object);

        return new ResponseBody(ContentType.JSON, content);
    }

    private static ResponseBody createErrorJsonResponse(final ControlServiceException exception) {
        final ServiceError error = exception.getError();

        return createErrorJsonResponse(error);
    }

    private static ResponseBody createErrorJsonResponse(final ServiceError error) {
        final ErrorResponse errorResponse = new ErrorResponse(error);

        return createJsonResponse(errorResponse);
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
