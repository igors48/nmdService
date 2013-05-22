package nmd.rss.collector.exporter;

import nmd.rss.collector.gae.feed.FeedServiceImpl;
import nmd.rss.collector.gae.feed.GaeFeedItemsRepository;
import nmd.rss.collector.updater.FeedItemsRepository;
import nmd.rss.collector.updater.FeedService;
import nmd.rss.collector.updater.InMemoryFeedHeadersAndUpdateTasksRepository;
import persistense.EMF;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static nmd.rss.collector.exporter.ExporterServletTools.exportFeed;
import static nmd.rss.collector.exporter.ExporterServletTools.parseFeedId;
import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.collector.util.CloseableTools.close;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 18.05.13
 */
public class ExporterServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ExporterServlet.class.getName());

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        assertNotNull(request);
        assertNotNull(response);

        final UUID feedId = parseFeedId(request.getPathInfo());

        if (feedId == null) {
            LOGGER.severe("Can not parse feed identifier");

            return;
        }

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/xml");

        final String generatedFeed = generateFeed(feedId);

        response.getWriter().print(generatedFeed);
    }

    private String generateFeed(final UUID feedId) {
        EntityManager entityManager = null;

        try {
            LOGGER.info(String.format("Try to generate feed for feed id [ %s ]", feedId));

            entityManager = EMF.get().createEntityManager();

            final InMemoryFeedHeadersAndUpdateTasksRepository feedHeadersAndUpdateTasksRepository = new InMemoryFeedHeadersAndUpdateTasksRepository();
            final FeedItemsRepository feedItemsRepository = new GaeFeedItemsRepository(entityManager);
            final FeedService feedService = new FeedServiceImpl(entityManager, feedItemsRepository, feedHeadersAndUpdateTasksRepository);

            return exportFeed(feedId, feedService);
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, String.format("Error generating feed for feed id [ %s ]", feedId), exception);

            return "";
        } finally {
            close(entityManager);
        }
    }

}
