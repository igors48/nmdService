package nmd.rss.collector.updater;

import nmd.rss.collector.gae.feed.FeedServiceImpl;
import nmd.rss.collector.gae.feed.GaeFeedItemsRepository;
import nmd.rss.collector.gae.fetcher.GaeUrlFetcher;
import nmd.rss.collector.scheduler.CycleFeedUpdateTaskScheduler;
import nmd.rss.collector.scheduler.FeedUpdateTaskScheduler;
import nmd.rss.collector.scheduler.FeedUpdateTaskSchedulerContextRepository;
import nmd.rss.collector.scheduler.InMemoryFeedUpdateTaskSchedulerContextRepository;
import persistense.EMF;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

import static nmd.rss.collector.util.Assert.assertNotNull;

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

        try {
            LOGGER.fine("Updater servlet initialization started");

            InMemoryFeedHeadersAndUpdateTasksRepository feedHeadersAndUpdateTasksRepository = new InMemoryFeedHeadersAndUpdateTasksRepository();
            FeedUpdateTaskSchedulerContextRepository contextRepository = new InMemoryFeedUpdateTaskSchedulerContextRepository();
            FeedUpdateTaskScheduler taskScheduler = new CycleFeedUpdateTaskScheduler(contextRepository, feedHeadersAndUpdateTasksRepository);

            EntityManager entityManager = EMF.get().createEntityManager();
            FeedItemsRepository feedItemsRepository = new GaeFeedItemsRepository(entityManager);
            FeedService feedService = new FeedServiceImpl(entityManager, feedItemsRepository, feedHeadersAndUpdateTasksRepository);

            UrlFetcher urlFetcher = new GaeUrlFetcher();

            LOGGER.fine("Updater servlet initialization completed. Updating started");

            FeedUpdater.update(taskScheduler, feedService, urlFetcher, 10);

            super.doGet(request, response);

            LOGGER.fine("Update completed");
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, "Update fault", exception);
        }
    }

}
