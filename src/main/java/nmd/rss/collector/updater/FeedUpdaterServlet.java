package nmd.rss.collector.updater;

import nmd.rss.collector.gae.feed.FeedServiceImpl;
import nmd.rss.collector.gae.feed.GaeFeedItemsRepository;
import nmd.rss.collector.gae.fetcher.GaeUrlFetcher;
import nmd.rss.collector.gae.updater.GaeFeedUpdateTaskSchedulerContextRepository;
import nmd.rss.collector.scheduler.*;
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
public class FeedUpdaterServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(FeedUpdaterServlet.class.getName());

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) {
        assertNotNull(request);
        assertNotNull(response);

        EntityManager entityManager = null;

        try {
            LOGGER.fine("Updater servlet initialization started");

            entityManager = EMF.get().createEntityManager();

            final InMemoryFeedHeadersAndUpdateTasksRepository feedHeadersAndUpdateTasksRepository = new InMemoryFeedHeadersAndUpdateTasksRepository();

            final FeedUpdateTaskSchedulerContextRepository contextRepository = new GaeFeedUpdateTaskSchedulerContextRepository(entityManager);
            final FeedUpdateTaskSchedulerContextService contextService = new FeedUpdateTaskSchedulerContextServiceImpl(entityManager, contextRepository);
            final FeedUpdateTaskScheduler taskScheduler = new CycleFeedUpdateTaskScheduler(contextService, feedHeadersAndUpdateTasksRepository);

            final FeedItemsRepository feedItemsRepository = new GaeFeedItemsRepository(entityManager);
            final FeedService feedService = new FeedServiceImpl(entityManager, feedItemsRepository, feedHeadersAndUpdateTasksRepository);

            final UrlFetcher urlFetcher = new GaeUrlFetcher();

            LOGGER.fine("Updater servlet initialization completed. Updating started");

            FeedUpdater.update(taskScheduler, feedService, urlFetcher, 10);

            LOGGER.fine("Update completed");
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, "Update fault", exception);
        } finally {
            close(entityManager);
        }
    }

}
