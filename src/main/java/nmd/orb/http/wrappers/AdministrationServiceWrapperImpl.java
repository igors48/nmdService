package nmd.orb.http.wrappers;

import nmd.orb.http.tools.ResponseBody;
import nmd.orb.services.AdministrationService;

import java.util.logging.Logger;

import static nmd.orb.http.responses.SuccessMessageResponse.create;
import static nmd.orb.http.tools.ResponseBody.createJsonResponse;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * @author : igu
 */
public class AdministrationServiceWrapperImpl implements AdministrationServiceWrapper {

    private static final Logger LOGGER = Logger.getLogger(AdministrationServiceWrapperImpl.class.getName());

    private final AdministrationService administrationService;

    public AdministrationServiceWrapperImpl(final AdministrationService administrationService) {
        guard(notNull(administrationService));
        this.administrationService = administrationService;
    }

    @Override
    public ResponseBody clear() {
        this.administrationService.reset();

        final String message = "Service cleared";

        LOGGER.info(message);

        return createJsonResponse(create(message));
    }

}
