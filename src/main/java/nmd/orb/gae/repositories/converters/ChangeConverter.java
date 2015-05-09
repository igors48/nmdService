package nmd.orb.gae.repositories.converters;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import nmd.orb.services.change.Event;
import nmd.orb.services.export.Change;

import java.util.ArrayList;

import static nmd.orb.gae.repositories.datastore.Kind.CHANGE;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Created by igor on 18.01.2015.
 */
public class ChangeConverter {

    private static final String TIMESTAMP = "timestamp";
    private static final String NOTIFICATION_IS_SENT = "notificationIsSent";

    public static Entity convert(final Change change, final Key key) {
        guard(notNull(change));
        guard(notNull(key));

        final Entity entity = new Entity(CHANGE.value, key);

        entity.setProperty(TIMESTAMP, change.getTimestamp());
        entity.setProperty(NOTIFICATION_IS_SENT, change.isNotificationIsSent());

        return entity;
    }

    public static Change convert(final Entity entity) {
        guard(notNull(entity));

        final long timestamp = (long) entity.getProperty(TIMESTAMP);
        final boolean notificationIsSent = (boolean) entity.getProperty(NOTIFICATION_IS_SENT);

        return new Change(timestamp, new ArrayList<Event>(), notificationIsSent);
    }

}
