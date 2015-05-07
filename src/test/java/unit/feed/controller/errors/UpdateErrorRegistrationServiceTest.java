package unit.feed.controller.errors;

import org.junit.Test;
import unit.feed.controller.AbstractControllerTestBase;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

/**
 * @author : igu
 */
public class UpdateErrorRegistrationServiceTest extends AbstractControllerTestBase {

    private static UUID FEED_ID = UUID.randomUUID();

    @Test
    public void whenErrorOccursThenCounterStarted() {
        this.updateErrorRegistrationService.updateError(FEED_ID);

        assertEquals(1, this.updateErrorRegistrationService.getErrorCount(FEED_ID));
    }

    @Test
    public void whenErrorOccursSequentallyThenCounterIncreased() {
        this.updateErrorRegistrationService.updateError(FEED_ID);
        this.updateErrorRegistrationService.updateError(FEED_ID);

        assertEquals(2, this.updateErrorRegistrationService.getErrorCount(FEED_ID));
    }

    @Test
    public void whenErrorOccursNotSequentallyThenCounterReset() {
        this.updateErrorRegistrationService.updateError(FEED_ID);
        this.updateErrorRegistrationService.updateError(FEED_ID);
        this.updateErrorRegistrationService.updateSuccess(FEED_ID);
        this.updateErrorRegistrationService.updateError(FEED_ID);

        assertEquals(1, this.updateErrorRegistrationService.getErrorCount(FEED_ID));
    }

    @Test
    public void whenErrorDeletedThenErrorCountIsZero() {
        this.updateErrorRegistrationService.updateError(FEED_ID);
        this.updateErrorRegistrationService.updateError(FEED_ID);

        this.updateErrorRegistrationService.delete(FEED_ID);

        assertEquals(0, this.updateErrorRegistrationService.getErrorCount(FEED_ID));
    }

}
