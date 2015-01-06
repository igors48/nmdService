package nmd.orb.http.servlets.importer;

import nmd.orb.http.Handler;
import nmd.orb.http.tools.ResponseBody;
import nmd.orb.http.wrappers.ImportServiceWrapper;
import nmd.orb.http.wrappers.ImportServiceWrapperImpl;

import java.util.List;
import java.util.Map;

import static nmd.orb.error.ServiceError.importJobInvalidAction;
import static nmd.orb.http.tools.ResponseBody.createErrorJsonResponse;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Created by igor on 07.12.2014.
 */
public class ImportServletPutRequestHandler implements Handler {

    public static final ImportServletPutRequestHandler IMPORT_SERVLET_PUT_REQUEST_HANDLER = new ImportServletPutRequestHandler(ImportServiceWrapperImpl.IMPORT_SERVICE_WRAPPER);

    private final ImportServiceWrapper importServiceWrapper;

    public ImportServletPutRequestHandler(final ImportServiceWrapper importServiceWrapper) {
        guard(notNull(importServiceWrapper));
        this.importServiceWrapper = importServiceWrapper;
    }

    //PUT -- /start - start import job
    //PUT -- /stop - stop import job
    @Override
    public ResponseBody handle(final List<String> elements, final Map<String, String> parameters, final String body) {
        guard(notNull(elements));
        guard(notNull(parameters));
        guard(notNull(body));

        final String action = elements.isEmpty() ? "" : elements.get(0);

        switch (action) {
            case "start": {
                return this.importServiceWrapper.start();
            }
            case "stop": {
                return this.importServiceWrapper.stop();
            }
        }

        return createErrorJsonResponse(importJobInvalidAction());
    }

}
