package nmd.orb.http.servlets;

import nmd.orb.http.BaseServlet;

import static nmd.orb.http.servlets.rss.ExportRssServletGetRequestHandler.EXPORT_RSS_SERVLET_GET_REQUEST_HANDLER;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 02.07.13
 */
public class ExportRssServlet extends BaseServlet {

    public ExportRssServlet() {
        super();

        this.handlers.put(GET, EXPORT_RSS_SERVLET_GET_REQUEST_HANDLER);
    }

}
