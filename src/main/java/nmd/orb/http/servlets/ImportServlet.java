package nmd.orb.http.servlets;

import nmd.orb.http.BaseServlet;

import static nmd.orb.http.servlets.importer.ImportServletDeleteRequestHandler.IMPORT_SERVLET_DELETE_REQUEST_HANDLER;
import static nmd.orb.http.servlets.importer.ImportServletGetRequestHandler.IMPORT_SERVLET_GET_REQUEST_HANDLER;
import static nmd.orb.http.servlets.importer.ImportServletPostRequestHandler.IMPORT_SERVLET_POST_REQUEST_HANDLER;
import static nmd.orb.http.servlets.importer.ImportServletPutRequestHandler.IMPORT_SERVLET_PUT_REQUEST_HANDLER;

/**
 * Created by igor on 07.12.2014.
 */
public class ImportServlet extends BaseServlet {

    public ImportServlet() {
        super();

        this.handlers.put(GET, IMPORT_SERVLET_GET_REQUEST_HANDLER);
        this.handlers.put(POST, IMPORT_SERVLET_POST_REQUEST_HANDLER);
        this.handlers.put(PUT, IMPORT_SERVLET_PUT_REQUEST_HANDLER);
        this.handlers.put(DELETE, IMPORT_SERVLET_DELETE_REQUEST_HANDLER);
    }

}
