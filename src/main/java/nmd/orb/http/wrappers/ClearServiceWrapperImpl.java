package nmd.orb.http.wrappers;

import nmd.orb.http.tools.ResponseBody;
import nmd.orb.services.ClearService;

import java.util.logging.Logger;

import static nmd.orb.gae.GaeServices.CLEAR_SERVICE;
import static nmd.orb.http.responses.SuccessMessageResponse.create;
import static nmd.orb.http.tools.ResponseBody.createJsonResponse;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * @author : igu
 */
public class ClearServiceWrapperImpl implements ClearServiceWrapper {

    public static final ClearServiceWrapperImpl CLEAR_SERVICE_WRAPPER = new ClearServiceWrapperImpl(CLEAR_SERVICE);

    private static final Logger LOGGER = Logger.getLogger(ClearServiceWrapperImpl.class.getName());

    private final ClearService clearService;

    public ClearServiceWrapperImpl(final ClearService clearService) {
        guard(notNull(clearService));
        this.clearService = clearService;
    }

    @Override
    public ResponseBody clear() {
        this.clearService.clear();

        final String message = "Service cleared";

        LOGGER.info(message);

        return createJsonResponse(create(message));
    }

}
