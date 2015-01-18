package nmd.orb.services.export;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.isPositive;

/**
 * Created by igor on 18.01.2015.
 */
public class Change {

    private final long timestamp;

    private boolean notificationIsSent;

    public Change(final long timestamp) {
        guard(isPositive(timestamp));
        this.timestamp = timestamp;

        this.notificationIsSent = false;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public boolean isNotificationIsSent() {
        return this.notificationIsSent;
    }

    public void setNotificationIsSent(final boolean notificationIsSent) {
        this.notificationIsSent = notificationIsSent;
    }

}
