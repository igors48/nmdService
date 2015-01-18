package unit.feed.controller.export;

import nmd.orb.services.export.Change;
import org.junit.Test;
import unit.feed.controller.AbstractControllerTestBase;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by igor on 18.01.2015.
 */
public class ChangeRegistrationTest extends AbstractControllerTestBase {

    @Test
    public void whenChangeIsRegisteredThenItStoresInRepository() {
        final long startTimestamp = System.currentTimeMillis();

        this.changeRegistrationService.registerChange();

        final Change change = this.changeRepositoryStub.load();

        assertTrue(change.getTimestamp() >= startTimestamp);
        assertFalse(change.isNotificationIsSent());
    }

}
