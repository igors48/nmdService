package nmd.orb.http.servlets;

import nmd.orb.gae.GaeWrappers;
import nmd.orb.http.BaseServlet;
import nmd.orb.http.servlets.export.ExportServletGetRequestHandler;

/**
 * @author : igu
 */
public class ExportServlet extends BaseServlet {

    public ExportServlet() {
        super();

        this.handlers.put(GET, new ExportServletGetRequestHandler(GaeWrappers.INSTANCE.getCategoriesServiceWrapper()));
    }

}
