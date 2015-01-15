package nmd.orb.http.servlets;

import nmd.orb.http.BaseServlet;

import static nmd.orb.http.servlets.export.ExportServletGetRequestHandler.EXPORT_SERVLET_GET_REQUEST_HANDLER;

/**
 * @author : igu
 */
public class ExportServlet extends BaseServlet {

    public ExportServlet() {
        super();

        this.handlers.put(GET, EXPORT_SERVLET_GET_REQUEST_HANDLER);
    }

}
