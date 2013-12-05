package nmd.rss.collector.rest;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static nmd.rss.collector.rest.ServletTools.writeException;
import static nmd.rss.collector.rest.ServletTools.writeResponseBody;

/**
 * User: igu
 * Date: 05.12.13
 */
public class RestServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ReadsServlet.class.getName());

    protected static final Map<String, Handler> HANDLERS = new HashMap<>();

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) {
        final Handler handler = HANDLERS.get(request.getMethod());

        try {
            if (handler == null) {
                super.service(request, response);
            } else {
                writeResponseBody(handler.handle(request), response);
            }
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, "Unhandled exception", exception);

            writeException(exception, response);
        }
    }

}
