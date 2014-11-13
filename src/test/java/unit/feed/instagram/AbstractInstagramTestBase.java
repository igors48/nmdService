package unit.feed.instagram;

import nmd.orb.error.ErrorCode;
import nmd.orb.error.ServiceError;
import nmd.orb.error.ServiceException;

import static org.junit.Assert.assertEquals;

/**
 * @author : igu
 */
public abstract class AbstractInstagramTestBase {

    protected void assertErrorOccured(final ServiceException exception, final ErrorCode expectedErrorCode) {
        final ServiceError serviceError = exception.getError();
        final ErrorCode errorCode = serviceError.code;

        assertEquals(expectedErrorCode, errorCode);
    }

}
