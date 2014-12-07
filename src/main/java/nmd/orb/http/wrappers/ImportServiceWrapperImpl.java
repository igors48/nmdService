package nmd.orb.http.wrappers;

import nmd.orb.error.ServiceException;
import nmd.orb.gae.GaeServices;
import nmd.orb.http.tools.ResponseBody;
import nmd.orb.services.ImportService;
import nmd.orb.services.importer.ImportJobContext;

import java.util.logging.Logger;

import static nmd.orb.http.responses.SuccessMessageResponse.create;
import static nmd.orb.http.tools.ResponseBody.createErrorJsonResponse;
import static nmd.orb.http.tools.ResponseBody.createJsonResponse;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Created by igor on 06.12.2014.
 */
public class ImportServiceWrapperImpl implements ImportServiceWrapper {

    public static final ImportServiceWrapperImpl IMPORT_SERVICE_WRAPPER = new ImportServiceWrapperImpl(GaeServices.IMPORT_SERVICE);

    private static final Logger LOGGER = Logger.getLogger(ImportServiceWrapperImpl.class.getName());

    private final ImportService importService;

    public ImportServiceWrapperImpl(final ImportService importService) {
        guard(notNull(importService));
        this.importService = importService;
    }

    @Override
    public ResponseBody schedule(final ImportJobContext context) {
        guard(notNull(context));

        try {
            this.importService.schedule(context);

            final String message = "Import job scheduled";

            LOGGER.info(message);

            return createJsonResponse(create(message));
        } catch (ServiceException exception) {
            return createErrorJsonResponse(exception);
        }
    }

    @Override
    public ResponseBody start() {
        this.importService.start();
        final String message = "Import job started";
        LOGGER.info(message);

        return createJsonResponse(create(message));
    }

    @Override
    public ResponseBody stop() {
        this.importService.stop();
        final String message = "Import job stopped";
        LOGGER.info(message);

        return createJsonResponse(create(message));
    }

    @Override
    public ResponseBody reject() {
        this.importService.reject();
        final String message = "Import job rejected";
        LOGGER.info(message);

        return createJsonResponse(create(message));
    }

    @Override
    public ResponseBody status() {
        return null;
    }

}
