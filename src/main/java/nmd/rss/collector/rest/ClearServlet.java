package nmd.rss.collector.rest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static nmd.rss.collector.rest.ServletTools.writeException;
import static nmd.rss.collector.rest.ServletTools.writeResponseBody;

/**
 * User: igu
 * Date: 29.11.13
 */
public class ClearServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ClearServlet.class.getName());

    // POST -- clear service
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

        try {
            final ResponseBody responseBody = ControlServiceWrapper.clear();

            writeResponseBody(responseBody, response);
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, "Unhandled exception", exception);

            writeException(exception, response);
        }
    }

}
