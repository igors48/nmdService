package nmd.orb.services.export;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.isPositive;

/**
 * Created by igor on 18.01.2015.
 */
public class Change {

    private final long timestamp;

    private boolean notificationIsSent;

    public Change(final long timestamp, final boolean notificationIsSent) {
        guard(isPositive(timestamp));
        this.timestamp = timestamp;

        this.notificationIsSent = notificationIsSent;
    }

    public Change(final long timestamp) {
        this(timestamp, false);
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public boolean isNotificationIsSent() {
        return this.notificationIsSent;
    }

    public Change markAsSent() {
        return new Change(this.timestamp, true);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Change change = (Change) o;

        if (notificationIsSent != change.notificationIsSent) return false;
        if (timestamp != change.timestamp) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (timestamp ^ (timestamp >>> 32));
        result = 31 * result + (notificationIsSent ? 1 : 0);
        return result;
    }

}
