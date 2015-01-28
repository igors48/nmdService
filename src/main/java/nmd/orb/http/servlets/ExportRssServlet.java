package nmd.orb.http.servlets;

import nmd.orb.gae.GaeWrappers;
import nmd.orb.http.BaseServlet;
import nmd.orb.http.servlets.export.ExportServletGetRequestHandler;

import static nmd.orb.http.servlets.rss.ExportRssServletGetRequestHandler.EXPORT_RSS_SERVLET_GET_REQUEST_HANDLER;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 02.07.13
 */
public class ExportRssServlet extends BaseServlet {

    public static final ExportServletGetRequestHandler EXPORT_SERVLET_GET_REQUEST_HANDLER = new ExportServletGetRequestHandler(GaeWrappers.INSTANCE.getCategoriesServiceWrapper());

    public ExportRssServlet() {
        super();

        this.handlers.put(GET, EXPORT_RSS_SERVLET_GET_REQUEST_HANDLER);
    }

}
