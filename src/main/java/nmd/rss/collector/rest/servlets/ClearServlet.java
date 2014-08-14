package nmd.rss.collector.rest.servlets;

import nmd.rss.collector.rest.BaseServlet;

import static nmd.rss.collector.rest.servlets.clear.ClearServletPostRequestHandler.CLEAR_SERVLET_POST_REQUEST_HANDLER;

/**
 * User: igu
 * Date: 29.11.13
 */
public class ClearServlet extends BaseServlet {

    public ClearServlet() {
        super();

        this.handlers.put(POST, CLEAR_SERVLET_POST_REQUEST_HANDLER);
    }

}
