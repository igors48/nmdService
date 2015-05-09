package unit.feed.controller.export;

import nmd.orb.services.change.AddCategoryEvent;
import nmd.orb.services.change.Event;
import nmd.orb.services.export.Change;
import org.junit.Test;
import unit.feed.controller.AbstractControllerTestBase;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by igor on 18.01.2015.
 */
public class ChangeRegistrationServiceTest extends AbstractControllerTestBase {

    @Test
    public void whenChangeIsRegisteredThenItStoresInRepository() {
        final long startTimestamp = System.currentTimeMillis();

        this.changeRegistrationService.registerAddCategory("category");

        final Change change = this.changeRepositoryStub.load();

        assertTrue(change.getTimestamp() >= startTimestamp);
        assertFalse(change.isNotificationIsSent());
    }

    @Test
    public void whenChangeNotificationIsNotSentThenEventAddedToIt() {
        this.changeRegistrationService.registerAddCategory("category-first");
        this.changeRegistrationService.registerAddCategory("category-second");

        final Change change = this.changeRepositoryStub.load();
        final List<Event> events = change.getEvents();

        assertEquals(2, events.size());
        assertEquals(new AddCategoryEvent("category-first"), events.get(0));
    }

    @Test
    public void whenChangeNotificationIsSentThenEventAddedToNewChange() {
        this.changeRegistrationService.registerAddCategory("category-first");

        final Change firstChange = this.changeRepositoryStub.load();
        this.changeRepositoryStub.store(firstChange.markAsSent());

        this.changeRegistrationService.registerAddCategory("category-second");

        final Change secondChange = this.changeRepositoryStub.load();
        final List<Event> events = secondChange.getEvents();

        assertEquals(1, events.size());
        assertEquals(new AddCategoryEvent("category-second"), events.get(0));
    }

}
