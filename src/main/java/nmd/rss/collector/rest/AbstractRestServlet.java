package nmd.rss.collector.rest;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

import static nmd.rss.collector.rest.ServletTools.writeException;
import static nmd.rss.collector.rest.ServletTools.writeResponseBody;

/**
 * User: igu
 * Date: 05.12.13
 */
public abstract class AbstractRestServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ReadsServlet.class.getName());

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) {

        try {
            final ResponseBody responseBody = createResponseBody(request);

            if (responseBody == null) {
                super.service(request, response);
            } else {
                writeResponseBody(responseBody, response);
            }
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, "Unhandled exception", exception);

            writeException(exception, response);
        }
    }

    protected ResponseBody handleGet(final HttpServletRequest request) {
        return null;
    }

    protected ResponseBody handlePost(final HttpServletRequest request) {
        return null;
    }

    protected ResponseBody handleDelete(final HttpServletRequest request) {
        return null;
    }

    private ResponseBody createResponseBody(final HttpServletRequest request) {

        switch (request.getMethod()) {
            case "GET": {
                return handleGet(request);
            }
            case "POST": {
                return handlePost(request);
            }
            case "DELETE": {
                return handleDelete(request);
            }
        }

        return null;
    }

}
