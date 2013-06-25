package nmd.rss.collector.updater;

import nmd.rss.collector.EntityManagerTransactions;
import nmd.rss.collector.gae.feed.FeedServiceImpl;
import nmd.rss.collector.gae.feed.GaeFeedItemsRepository;
import nmd.rss.collector.gae.fetcher.GaeUrlFetcher;
import nmd.rss.collector.gae.updater.GaeFeedUpdateTaskSchedulerContextRepository;
import nmd.rss.collector.scheduler.CycleFeedUpdateTaskScheduler;
import nmd.rss.collector.scheduler.FeedUpdateTaskScheduler;
import nmd.rss.collector.scheduler.FeedUpdateTaskSchedulerContextRepository;
import nmd.rss.collector.gae.EMF;

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

            final EntityManagerTransactions entityManagerTransactions = new EntityManagerTransactions(entityManager);
            final InMemoryFeedHeadersAndUpdateTasksRepository feedHeadersAndUpdateTasksRepository = new InMemoryFeedHeadersAndUpdateTasksRepository();

            final FeedUpdateTaskSchedulerContextRepository contextRepository = new GaeFeedUpdateTaskSchedulerContextRepository(entityManager);
            final FeedUpdateTaskScheduler taskScheduler = new CycleFeedUpdateTaskScheduler(contextRepository, feedHeadersAndUpdateTasksRepository, entityManagerTransactions);

            final FeedItemsRepository feedItemsRepository = new GaeFeedItemsRepository(entityManager);
            final FeedService feedService = new FeedServiceImpl(entityManager, feedItemsRepository, feedHeadersAndUpdateTasksRepository);

            final UrlFetcher urlFetcher = new GaeUrlFetcher();

            LOGGER.fine("Updater servlet initialization completed. Updating started");

            FeedUpdater.update(taskScheduler, feedService, urlFetcher);

            LOGGER.fine("Update completed");
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, "Update fault", exception);
        } finally {
            close(entityManager);
        }
    }

}
