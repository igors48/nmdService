package cron;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import persistense.EMF;
import persistense.Record;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Logger;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 26.04.13
 */
public class CronServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(CronServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        LOGGER.info(String.format("doGet [ %d ]", System.currentTimeMillis()));

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        try {
            URL url = new URL("http://www.3dnews.ru/news/rss");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(reader);

            String line = String.format("[ %d ] feed items was read", feed.getEntries().size());
            response.getWriter().println(line);

            reader.close();

            EntityManager entityManager = EMF.get().createEntityManager();

            try {
                Record record = new Record(line + Long.toString(System.currentTimeMillis()));
                entityManager.persist(record);
            } catch (Exception e) {
                throw new ServletException(e);
            } finally {
                entityManager.close();
            }

        } catch (IOException | FeedException exception) {
            throw new ServletException(exception);
        }
    }

}
