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

    public FeedsServlet() {
        super();

        this.handlers.put(GET, new FeedsServletGetRequestHandler(GaeWrappers.INSTANCE.getFeedsServiceWrapper()));
        this.handlers.put(POST, new FeedsServletPostRequestHandler(GaeWrappers.INSTANCE.getFeedsServiceWrapper()));
        this.handlers.put(PUT, new FeedsServletPutRequestHandler(GaeWrappers.INSTANCE.getFeedsServiceWrapper()));
        this.handlers.put(DELETE, new FeedsServletDeleteRequestHandler(GaeWrappers.INSTANCE.getFeedsServiceWrapper()));
    }

}
