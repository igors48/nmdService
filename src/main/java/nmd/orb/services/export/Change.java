package nmd.orb.services.export;

import nmd.orb.services.change.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.isPositive;
import static nmd.orb.util.Parameter.notNull;

/**
 * Created by igor on 18.01.2015.
 */
public class Change {

    private final long timestamp;
    private final List<Event> events;
    private final boolean notificationIsSent;

    public Change(final long timestamp, final List<Event> events, final boolean notificationIsSent) {
        guard(isPositive(this.timestamp = timestamp));
        guard(notNull(this.events = events));
        this.notificationIsSent = notificationIsSent;
    }

    public Change(final long timestamp) {
        this(timestamp, new ArrayList<Event>(), false);
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public boolean isNotificationIsSent() {
        return this.notificationIsSent;
    }

    public Change markAsSent() {
        return new Change(this.timestamp, this.events, true);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Change change = (Change) o;
        return Objects.equals(timestamp, change.timestamp) &&
                Objects.equals(notificationIsSent, change.notificationIsSent) &&
                Objects.equals(events, change.events);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, events, notificationIsSent);
    }

}
