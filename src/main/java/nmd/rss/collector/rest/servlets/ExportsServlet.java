package nmd.rss.collector.rest.servlets;

import nmd.rss.collector.rest.BaseServlet;
import nmd.rss.collector.rest.servlets.exports.ExportsServletGetRequestHandler;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 02.07.13
 */
public class ExportsServlet extends BaseServlet {

    public ExportsServlet() {
        super();

        this.handlers.put(GET, ExportsServletGetRequestHandler.EXPORTS_SERVLET_GET_REQUEST_HANDLER);
    }

}
