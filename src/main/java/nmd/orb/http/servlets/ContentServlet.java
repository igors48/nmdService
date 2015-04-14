package nmd.orb.http.servlets;

import nmd.orb.gae.GaeWrappers;
import nmd.orb.http.BaseServlet;
import nmd.orb.http.servlets.content.ContentServletGetRequestHandler;

/**
 * @author : igu
 */
public class ContentServlet extends BaseServlet {

    public ContentServlet() {
        super();

        this.handlers.put(GET, new ContentServletGetRequestHandler(GaeWrappers.INSTANCE.getContentFilterServiceWrapper()));
    }

}
