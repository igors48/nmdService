package nmd.orb.http.servlets;

import nmd.orb.gae.GaeWrappers;
import nmd.orb.http.BaseServlet;
import nmd.orb.http.servlets.rss.ExportRssServletGetRequestHandler;


/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 02.07.13
 */
public class ExportRssServlet extends BaseServlet {

    public ExportRssServlet() {
        super();

        this.handlers.put(GET, new ExportRssServletGetRequestHandler(GaeWrappers.INSTANCE.getFeedsServiceWrapper()));
    }

}
