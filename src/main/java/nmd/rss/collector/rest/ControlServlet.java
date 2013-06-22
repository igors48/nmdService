package nmd.rss.collector.rest;

import nmd.rss.collector.EntityManagerTransactions;
import nmd.rss.collector.Transactions;
import nmd.rss.collector.controller.ControlService;
import nmd.rss.collector.controller.ControllerException;
import nmd.rss.collector.exporter.ExporterServletTools;
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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static nmd.rss.collector.util.CloseableTools.close;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.06.13
 */
public class ControlServlet extends HttpServlet {

    // GET /feeds -- feeds list
    // GET /feeds/{feedId} -- get feed
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final EntityManager entityManager = EMF.get().createEntityManager();

        try {
            final UUID feedId = ExporterServletTools.parseFeedId(request.getPathInfo());
            final ControlService controlService = createControlService(entityManager);

            if (feedId == null) {
                final List<FeedHeader> feedHeaders = controlService.getFeedHeaders();
                writeFeedHeadersToResponse(feedHeaders, response);
            } else {
                final Feed feed = controlService.getFeed(feedId);
                writeFeedToResponse(feed, response);
            }

        } catch (ControllerException controllerException) {
            writeControllerExceptionToResponse(response);
        } catch (Exception exception) {
            writeExceptionToResponse(response);
        } finally {
            close(entityManager);
        }
    }

    // POST /feeds -- add feed
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        super.doPost(request, response);
    }

    // DELETE /feeds/{feedId} -- delete feed
    @Override
    protected void doDelete(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        super.doDelete(request, response);
    }

    private static ControlService createControlService(final EntityManager entityManager) {
        final Transactions transactions = new EntityManagerTransactions(entityManager);
        final UrlFetcher urlFetcher = new GaeUrlFetcher();
        final FeedUpdateTaskRepository feedUpdateTaskRepository = new GaeFeedUpdateTaskRepository(entityManager);
        final FeedItemsRepository feedItemsRepository = new GaeFeedItemsRepository(entityManager);
        final FeedHeadersRepository feedHeadersRepository = new GaeFeedHeadersRepository(entityManager);

        return new ControlService(feedHeadersRepository, feedItemsRepository, feedUpdateTaskRepository, urlFetcher, transactions);
    }

    private static void writeExceptionToResponse(final HttpServletResponse response) {
    }

    private static void writeControllerExceptionToResponse(final HttpServletResponse response) {
    }

    private static void writeFeedToResponse(final Feed feed, final HttpServletResponse response) {
    }

    private static void writeFeedHeadersToResponse(final List<FeedHeader> feedHeaders, final HttpServletResponse response) {
    }

}
