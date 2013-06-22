package nmd.rss.collector.rest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.06.13
 */
public class ControlServlet extends HttpServlet {

    // GET /feeds -- feeds list
    // GET /feeds/{feedId} -- get feed
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        super.doGet(request, response);
    }

    // POST /feeds -- add feed
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        super.doPost(request, response);
    }

    // DELETE /feeds/{feedId} -- delete feed
    @Override
    protected void doDelete(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        super.doDelete(request, response);
    }

}
