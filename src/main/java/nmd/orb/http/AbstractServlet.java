package nmd.orb.http;

import nmd.orb.http.servlets.ReadsServlet;
import nmd.orb.http.tools.ResponseBody;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

import static nmd.orb.http.tools.ServletTools.writeException;
import static nmd.orb.http.tools.ServletTools.writeResponseBody;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date: 05.12.13
 */
public abstract class AbstractServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ReadsServlet.class.getName());

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) {
        final long startTime = System.currentTimeMillis();

        try {
            final ResponseBody responseBody = createResponseBody(request);

            if (responseBody == null) {
                super.service(request, response);
            } else {
                writeResponseBody(startTime, responseBody, response);
            }
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, "Unhandled exception", exception);

            writeException(startTime, exception, response);
        }
    }

    protected ResponseBody handleGet(final HttpServletRequest request) {
        return null;
    }

    protected ResponseBody handlePost(final HttpServletRequest request) {
        return null;
    }

    protected ResponseBody handlePut(final HttpServletRequest request) {
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
            case "PUT": {
                return handlePut(request);
            }
            case "DELETE": {
                return handleDelete(request);
            }
        }

        return null;
    }

}
