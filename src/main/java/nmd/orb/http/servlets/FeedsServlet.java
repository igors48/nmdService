package nmd.orb.http.servlets;

import nmd.orb.gae.GaeWrappers;
import nmd.orb.http.BaseServlet;
import nmd.orb.http.servlets.feeds.FeedsServletDeleteRequestHandler;
import nmd.orb.http.servlets.feeds.FeedsServletGetRequestHandler;
import nmd.orb.http.servlets.feeds.FeedsServletPostRequestHandler;
import nmd.orb.http.servlets.feeds.FeedsServletPutRequestHandler;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.06.13
 */
public class FeedsServlet extends BaseServlet {

    private static final FeedsServletPostRequestHandler FEEDS_SERVLET_POST_REQUEST_HANDLER = new FeedsServletPostRequestHandler(GaeWrappers.INSTANCE.getFeedsServiceWrapper());
    private static final FeedsServletDeleteRequestHandler FEEDS_SERVLET_DELETE_REQUEST_HANDLER = new FeedsServletDeleteRequestHandler(GaeWrappers.INSTANCE.getFeedsServiceWrapper());
    public static final FeedsServletGetRequestHandler FEEDS_SERVLET_GET_REQUEST_HANDLER = new FeedsServletGetRequestHandler(GaeWrappers.INSTANCE.getFeedsServiceWrapper());
    public static final FeedsServletPutRequestHandler FEEDS_SERVLET_PUT_REQUEST_HANDLER = new FeedsServletPutRequestHandler(GaeWrappers.INSTANCE.getFeedsServiceWrapper());

    public FeedsServlet() {
        super();

        this.handlers.put(GET, FEEDS_SERVLET_GET_REQUEST_HANDLER);
        this.handlers.put(POST, FEEDS_SERVLET_POST_REQUEST_HANDLER);
        this.handlers.put(PUT, FEEDS_SERVLET_PUT_REQUEST_HANDLER);
        this.handlers.put(DELETE, FEEDS_SERVLET_DELETE_REQUEST_HANDLER);
    }

}
