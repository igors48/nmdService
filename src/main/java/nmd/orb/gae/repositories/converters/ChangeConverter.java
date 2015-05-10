package nmd.orb.gae.repositories.converters;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import nmd.orb.gae.repositories.converters.helpers.EventTypeAdapter;
import nmd.orb.services.change.Event;
import nmd.orb.services.export.Change;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static nmd.orb.gae.repositories.datastore.Kind.CHANGE;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Created by igor on 18.01.2015.
 */
public class ChangeConverter {

    private static final String TIMESTAMP = "timestamp";
    private static final String NOTIFICATION_IS_SENT = "notificationIsSent";
    private static final String EVENTS = "events";

    private static final Type EVENT_LIST_TYPE = new TypeToken<ArrayList<Event>>() {
    }.getType();

    private static Gson GSON;

    static {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Event.class, new EventTypeAdapter<>());

        GSON = gsonBuilder.create();
    }

    public static Entity convert(final Change change, final Key key) {
        guard(notNull(change));
        guard(notNull(key));

        final Entity entity = new Entity(CHANGE.value, key);

        entity.setProperty(TIMESTAMP, change.getTimestamp());
        entity.setProperty(NOTIFICATION_IS_SENT, change.isNotificationIsSent());

        final String data = GSON.toJson(change.getEvents(), EVENT_LIST_TYPE);
        entity.setProperty(EVENTS, data);

        return entity;
    }

    public static Change convert(final Entity entity) {
        guard(notNull(entity));

        final long timestamp = (long) entity.getProperty(TIMESTAMP);
        final boolean notificationIsSent = (boolean) entity.getProperty(NOTIFICATION_IS_SENT);
        final List<Event> events = GSON.fromJson((String) entity.getProperty(EVENTS), EVENT_LIST_TYPE);

        return new Change(timestamp, events, notificationIsSent);
    }

}
