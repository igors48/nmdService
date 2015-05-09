package unit.feed.controller.export;

import nmd.orb.services.change.AddCategoryEvent;
import nmd.orb.services.export.Change;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by igor on 09.05.2015.
 */
public class ChangeTest {

    @Test
    public void whenEventAddedThenTimestampUpdated() {
        final long firstTimestamp = 1;
        final Change change = new Change(firstTimestamp);

        final long secondTimestamp = 2;
        change.addEvent(secondTimestamp, new AddCategoryEvent("category"));

        assertEquals(secondTimestamp, change.getTimestamp());
    }

    @Test
    public void whenEventAddedThenEventListUpdated() {
        final long firstTimestamp = 1;
        final Change change = new Change(firstTimestamp);

        final long secondTimestamp = 2;
        final AddCategoryEvent event = new AddCategoryEvent("category");
        change.addEvent(secondTimestamp, event);

        assertEquals(1, change.getEvents().size());
        assertEquals(event, change.getEvents().get(0));
    }

}
