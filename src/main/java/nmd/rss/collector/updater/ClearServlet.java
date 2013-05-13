package nmd.rss.collector.updater;

import nmd.rss.collector.gae.feed.FeedServiceImpl;
import nmd.rss.collector.gae.feed.GaeFeedItemsRepository;
import persistense.EMF;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.collector.util.CloseableTools.close;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 09.05.13
 */
public class ClearServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ClearServlet.class.getName());

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) {
        assertNotNull(request);
        assertNotNull(response);

        EntityManager entityManager = null;

        try {
            LOGGER.fine("Clear servlet initialization started");

            final InMemoryFeedHeadersAndUpdateTasksRepository feedHeadersAndUpdateTasksRepository = new InMemoryFeedHeadersAndUpdateTasksRepository();

            entityManager = EMF.get().createEntityManager();

            final FeedItemsRepository feedItemsRepository = new GaeFeedItemsRepository(entityManager);
            final FeedService feedService = new FeedServiceImpl(entityManager, feedItemsRepository, feedHeadersAndUpdateTasksRepository);

            feedService.clearAll();

            LOGGER.fine("Clear servlet completed");
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, "Clear servlet fault", exception);
        } finally {
            close(entityManager);
        }
    }

}
