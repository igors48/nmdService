package cron;

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.info(String.format("doGet [ %d ]", System.currentTimeMillis()));

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        try {
            URL url = new URL("http://www.3dnews.ru/news/rss");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;

            //while ((line = reader.readLine()) != null) {
            //  response.getWriter().println(line);
            //}

            line = reader.readLine();
            line = line == null ? "null" : line;

            response.getWriter().println(line);

            reader.close();
        } catch (IOException e) {
            // ...
        }
    }

}
