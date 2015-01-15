package nmd.orb.http.wrappers;

import nmd.orb.http.tools.ResponseBody;
import nmd.orb.services.ResetService;

import java.util.logging.Logger;

import static nmd.orb.gae.GaeServices.CLEAR_SERVICE;
import static nmd.orb.http.responses.SuccessMessageResponse.create;
import static nmd.orb.http.tools.ResponseBody.createJsonResponse;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * @author : igu
 */
public class ResetServiceWrapperImpl implements ResetServiceWrapper {

    public static final ResetServiceWrapperImpl RESET_SERVICE_WRAPPER = new ResetServiceWrapperImpl(CLEAR_SERVICE);

    private static final Logger LOGGER = Logger.getLogger(ResetServiceWrapperImpl.class.getName());

    private final ResetService resetService;

    public ResetServiceWrapperImpl(final ResetService resetService) {
        guard(notNull(resetService));
        this.resetService = resetService;
    }

    @Override
    public ResponseBody clear() {
        this.resetService.reset();

        final String message = "Service cleared";

        LOGGER.info(message);

        return createJsonResponse(create(message));
    }

}
