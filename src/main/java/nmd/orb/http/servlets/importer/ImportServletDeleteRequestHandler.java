package nmd.orb.http.servlets.importer;

import nmd.orb.http.Handler;
import nmd.orb.http.tools.ResponseBody;
import nmd.orb.http.wrappers.ImportServiceWrapper;
import nmd.orb.http.wrappers.ImportServiceWrapperImpl;

import java.util.List;
import java.util.Map;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Created by igor on 07.12.2014.
 */
public class ImportServletDeleteRequestHandler implements Handler {

    public static final ImportServletDeleteRequestHandler IMPORT_SERVLET_DELETE_REQUEST_HANDLER = new ImportServletDeleteRequestHandler(ImportServiceWrapperImpl.IMPORT_SERVICE_WRAPPER);

    private final ImportServiceWrapper importServiceWrapper;

    public ImportServletDeleteRequestHandler(final ImportServiceWrapper importServiceWrapper) {
        guard(notNull(importServiceWrapper));
        this.importServiceWrapper = importServiceWrapper;
    }

    //DELETE -- reject import job
    @Override
    public ResponseBody handle(final List<String> elements, final Map<String, String> parameters, final String body) {
        guard(notNull(elements));
        guard(notNull(parameters));
        guard(notNull(body));

        return this.importServiceWrapper.reject();
    }

}
