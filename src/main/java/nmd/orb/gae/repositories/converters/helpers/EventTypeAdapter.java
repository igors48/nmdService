package nmd.orb.gae.repositories.converters.helpers;

import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import nmd.orb.services.change.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Created by igor on 10.05.2015.
 */
public class EventTypeAdapter<Event> extends TypeAdapter<Event> {

    private static final String NAME_FIELD = "name";
    private static final String DATA_FIELD = "data";

    private static Map<String, Class> classMap;

    static {
        classMap = new HashMap<>();

        classMap.put(AddCategoryEvent.class.getSimpleName(), AddCategoryEvent.class);
        classMap.put(AddFeedEvent.class.getSimpleName(), AddFeedEvent.class);
        classMap.put(AssignFeedToCategoryEvent.class.getSimpleName(), AssignFeedToCategoryEvent.class);
        classMap.put(DeleteCategoryEvent.class.getSimpleName(), DeleteCategoryEvent.class);
        classMap.put(RemoveFeedEvent.class.getSimpleName(), RemoveFeedEvent.class);
        classMap.put(RenameCategoryEvent.class.getSimpleName(), RenameCategoryEvent.class);
        classMap.put(RenameFeedEvent.class.getSimpleName(), RenameFeedEvent.class);
    }

    private static final Gson GSON = new Gson();

    @Override
    public void write(final JsonWriter jsonWriter, final Event event) throws IOException {
        guard(notNull(jsonWriter));
        guard(notNull(event));

        jsonWriter.beginObject();

        jsonWriter.name(NAME_FIELD).value(event.getClass().getSimpleName());
        jsonWriter.name(DATA_FIELD).value(GSON.toJson(event));

        jsonWriter.endObject();
    }

    @Override
    public Event read(final JsonReader jsonReader) throws IOException {
        guard(notNull(jsonReader));

        jsonReader.beginObject();

        String name = "";
        String data = "";

        while (jsonReader.hasNext()) {

            switch (jsonReader.nextName()) {
                case NAME_FIELD: {
                    name = jsonReader.nextString();
                    break;
                }
                case DATA_FIELD: {
                    data = jsonReader.nextString();
                    break;
                }
            }
        }

        jsonReader.endObject();

        final Class clazz = classMap.get(name);
        final Event event = (Event) GSON.fromJson(data, clazz);

        return event;
    }

}
