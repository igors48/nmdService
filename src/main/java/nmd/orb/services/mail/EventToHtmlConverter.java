package nmd.orb.services.mail;

import nmd.orb.services.change.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Created by igor on 10.05.2015.
 */
public class EventToHtmlConverter {

    private static Map<Class, Converter> converters;

    static {
        converters = new HashMap<>();

        converters.put(AddCategoryEvent.class, new Converter<AddCategoryEvent>() {
            @Override
            public String convert(final AddCategoryEvent event) {
                return format("Category <b>%s</b> added", event.getCategoryName());
            }
        });

        converters.put(AddFeedEvent.class, new Converter<AddFeedEvent>() {
            @Override
            public String convert(final AddFeedEvent event) {
                return format("Feed <b>%s</b> added to category <b>%s</b>", event.getFeedTitle(), event.getCategoryName());
            }
        });

        converters.put(AssignFeedToCategoryEvent.class, new Converter<AssignFeedToCategoryEvent>() {
            @Override
            public String convert(final AssignFeedToCategoryEvent event) {
                return format("Feed <b>%s</b> assigned to category <b>%s</b>", event.getFeedTitle(), event.getCategoryName());
            }
        });

        converters.put(DeleteCategoryEvent.class, new Converter<DeleteCategoryEvent>() {
            @Override
            public String convert(final DeleteCategoryEvent event) {
                return format("Category <b>%s</b> deleted", event.getCategoryName());
            }
        });

        converters.put(RemoveFeedEvent.class, new Converter<RemoveFeedEvent>() {
            @Override
            public String convert(final RemoveFeedEvent event) {
                return format("Feed <b>%s</b> deleted", event.getFeedTitle());
            }
        });

        converters.put(RenameCategoryEvent.class, new Converter<RenameCategoryEvent>() {
            @Override
            public String convert(final RenameCategoryEvent event) {
                return format("Category <b>%s</b> renamed to <b>%s</b>", event.getOldCategoryName(), event.getNewCategoryName());
            }
        });

        converters.put(RenameFeedEvent.class, new Converter<RenameFeedEvent>() {
            @Override
            public String convert(final RenameFeedEvent event) {
                return format("Feed <b>%s</b> renamed to <b>%s</b>", event.getOldFeedTitle(), event.getNewFeedTitle());
            }
        });

    }

    public static String convert(final List<Event> events) {
        guard(notNull(events));

        String eventsList = "";

        for (final Event event : events) {
            eventsList += format("<li>%s</li>", convert(event));
        }

        return format("<p>Latest changes</p><ol>%s</ol>", eventsList);
    }

    public static String convert(final Event event) {
        guard(notNull(event));

        final Converter converter = converters.get(event.getClass());

        return converter == null ? "" : converter.convert(event);
    }

    private interface Converter<T extends Event> {

        String convert(T event);

    }

}
