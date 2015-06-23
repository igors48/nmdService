package unit.feed.controller.errors;

import nmd.orb.error.ServiceError;
import nmd.orb.services.update.UpdateErrors;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author : igu
 */
public class UpdateErrorsListTest {

    private static final int MAX_STORED_ERRORS_COUNT = 2;
    private static final ServiceError FIRST_ERROR = ServiceError.feedParseError("http:\\domain.com");
    private static final ServiceError SECOND_ERROR = ServiceError.feedParseError("http:\\domain2.com");
    private static final ServiceError THIRD_ERROR = ServiceError.feedParseError("http:\\domain3.com");

    @Test
    public void whenCountOfStoredErrorsLesserThanMaximumThenAllErrorsAreStored() {
        UpdateErrors updateErrors = new UpdateErrors(UUID.randomUUID(), MAX_STORED_ERRORS_COUNT);

        updateErrors = updateErrors.appendError(FIRST_ERROR);

        final List<ServiceError> errors = updateErrors.errors;

        assertEquals(1, errors.size());
        assertTrue(errors.contains(FIRST_ERROR));
    }

    @Test
    public void whenCountOfStoredErrorsEqualToMaximumThenAllErrorsAreStored() {
        UpdateErrors updateErrors = new UpdateErrors(UUID.randomUUID(), MAX_STORED_ERRORS_COUNT);

        updateErrors = updateErrors.appendError(FIRST_ERROR);
        updateErrors = updateErrors.appendError(SECOND_ERROR);

        final List<ServiceError> errors = updateErrors.errors;

        assertEquals(MAX_STORED_ERRORS_COUNT, errors.size());
        assertTrue(errors.contains(FIRST_ERROR));
        assertTrue(errors.contains(SECOND_ERROR));
    }

    @Test
    public void whenCountOfStoredErrorsGreaterThanMaximumThenOnlyLatestErrorsAreStored() {
        UpdateErrors updateErrors = new UpdateErrors(UUID.randomUUID(), MAX_STORED_ERRORS_COUNT);

        updateErrors = updateErrors.appendError(FIRST_ERROR);
        updateErrors = updateErrors.appendError(SECOND_ERROR);
        updateErrors = updateErrors.appendError(THIRD_ERROR);

        final List<ServiceError> errors = updateErrors.errors;

        assertEquals(MAX_STORED_ERRORS_COUNT, errors.size());
        assertTrue(errors.contains(SECOND_ERROR));
        assertTrue(errors.contains(THIRD_ERROR));
    }

}
