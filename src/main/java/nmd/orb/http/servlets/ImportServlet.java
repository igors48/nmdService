package nmd.orb.http.servlets;

import nmd.orb.gae.GaeWrappers;
import nmd.orb.http.BaseServlet;
import nmd.orb.http.servlets.importer.ImportServletDeleteRequestHandler;
import nmd.orb.http.servlets.importer.ImportServletGetRequestHandler;
import nmd.orb.http.servlets.importer.ImportServletPostRequestHandler;
import nmd.orb.http.servlets.importer.ImportServletPutRequestHandler;

/**
 * Created by igor on 07.12.2014.
 */
public class ImportServlet extends BaseServlet {

    public static final ImportServletDeleteRequestHandler IMPORT_SERVLET_DELETE_REQUEST_HANDLER = new ImportServletDeleteRequestHandler(GaeWrappers.INSTANCE.getImportServiceWrapper());
    public static final ImportServletGetRequestHandler IMPORT_SERVLET_GET_REQUEST_HANDLER = new ImportServletGetRequestHandler(GaeWrappers.INSTANCE.getImportServiceWrapper());
    public static final ImportServletPostRequestHandler IMPORT_SERVLET_POST_REQUEST_HANDLER = new ImportServletPostRequestHandler(GaeWrappers.INSTANCE.getImportServiceWrapper());
    public static final ImportServletPutRequestHandler IMPORT_SERVLET_PUT_REQUEST_HANDLER = new ImportServletPutRequestHandler(GaeWrappers.INSTANCE.getImportServiceWrapper());

    public ImportServlet() {
        super();

        this.handlers.put(GET, IMPORT_SERVLET_GET_REQUEST_HANDLER);
        this.handlers.put(POST, IMPORT_SERVLET_POST_REQUEST_HANDLER);
        this.handlers.put(PUT, IMPORT_SERVLET_PUT_REQUEST_HANDLER);
        this.handlers.put(DELETE, IMPORT_SERVLET_DELETE_REQUEST_HANDLER);
    }

}
