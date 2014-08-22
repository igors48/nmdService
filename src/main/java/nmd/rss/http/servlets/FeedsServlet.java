package nmd.rss.http.servlets;

import nmd.rss.http.BaseServlet;

import static nmd.rss.http.servlets.feeds.FeedsServletDeleteRequestHandler.FEEDS_SERVLET_DELETE_REQUEST_HANDLER;
import static nmd.rss.http.servlets.feeds.FeedsServletGetRequestHandler.FEEDS_SERVLET_GET_REQUEST_HANDLER;
import static nmd.rss.http.servlets.feeds.FeedsServletPostRequestHandler.FEEDS_SERVLET_POST_REQUEST_HANDLER;
import static nmd.rss.http.servlets.feeds.FeedsServletPutRequestHandler.FEEDS_SERVLET_PUT_REQUEST_HANDLER;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.06.13
 */
public class FeedsServlet extends BaseServlet {

    public FeedsServlet() {
        super();

        this.handlers.put(GET, FEEDS_SERVLET_GET_REQUEST_HANDLER);
        this.handlers.put(POST, FEEDS_SERVLET_POST_REQUEST_HANDLER);
        this.handlers.put(PUT, FEEDS_SERVLET_PUT_REQUEST_HANDLER);
        this.handlers.put(DELETE, FEEDS_SERVLET_DELETE_REQUEST_HANDLER);
    }

}
