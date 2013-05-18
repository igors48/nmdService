package nmd.rss.collector.exporter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 18.05.13
 */
public class ExporterServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ExporterServlet.class.getName());

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        assertNotNull(request);
        assertNotNull(response);

        //TODO create utility method for parse feedId from request`s pathInfo
        String pathInfo = request.getPathInfo();
        System.out.println(pathInfo);

        super.doGet(request, response);
    }

}
