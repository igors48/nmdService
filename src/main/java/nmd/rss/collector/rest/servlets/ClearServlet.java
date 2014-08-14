package nmd.rss.collector.rest.servlets;

import nmd.rss.collector.rest.BaseServlet;
import nmd.rss.collector.rest.servlets.clear.ClearServletPostRequestHandler;

/**
 * User: igu
 * Date: 29.11.13
 */
public class ClearServlet extends BaseServlet {

    public ClearServlet() {
        super();

        this.handlers.put(POST, ClearServletPostRequestHandler.CLEAR_SERVLET_POST_REQUEST_HANDLER);
    }

}
