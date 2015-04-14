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

    public ImportServlet() {
        super();

        this.handlers.put(GET, new ImportServletGetRequestHandler(GaeWrappers.INSTANCE.getImportServiceWrapper()));
        this.handlers.put(POST, new ImportServletPostRequestHandler(GaeWrappers.INSTANCE.getImportServiceWrapper()));
        this.handlers.put(PUT, new ImportServletPutRequestHandler(GaeWrappers.INSTANCE.getImportServiceWrapper()));
        this.handlers.put(DELETE, new ImportServletDeleteRequestHandler(GaeWrappers.INSTANCE.getImportServiceWrapper()));
    }

}
