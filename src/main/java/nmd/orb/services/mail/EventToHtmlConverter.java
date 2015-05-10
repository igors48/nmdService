package nmd.orb.services.mail;

import nmd.orb.services.change.*;

import java.util.HashMap;
import java.util.Map;

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
                return String.format("Category <b>%s</b> added", event.categoryName);
            }
        });

        converters.put(AddFeedEvent.class, new Converter<AddFeedEvent>() {
            @Override
            public String convert(final AddFeedEvent event) {
                return String.format("Feed <b>%s</b> added to category <b>%s</b>", event.feedTitle, event.categoryName);
            }
        });

        converters.put(AssignFeedToCategoryEvent.class, new Converter<AssignFeedToCategoryEvent>() {
            @Override
            public String convert(final AssignFeedToCategoryEvent event) {
                return String.format("Feed <b>%s</b> assigned to category <b>%s</b>", event.feedTitle, event.categoryName);
            }
        });

        converters.put(DeleteCategoryEvent.class, new Converter<DeleteCategoryEvent>() {
            @Override
            public String convert(final DeleteCategoryEvent event) {
                return String.format("Category <b>%s</b> deleted", event.categoryName);
            }
        });

        converters.put(RemoveFeedEvent.class, new Converter<RemoveFeedEvent>() {
            @Override
            public String convert(final RemoveFeedEvent event) {
                return String.format("Feed <b>%s</b> deleted", event.feedTitle);
            }
        });

        converters.put(RenameCategoryEvent.class, new Converter<RenameCategoryEvent>() {
            @Override
            public String convert(final RenameCategoryEvent event) {
                return String.format("Category <b>%s</b> renamed to <b>%s</b>", event.oldCategoryName, event.newCategoryName);
            }
        });

        converters.put(RenameFeedEvent.class, new Converter<RenameFeedEvent>() {
            @Override
            public String convert(final RenameFeedEvent event) {
                return String.format("Feed <b>%s</b> renamed to <b>%s</b>", event.oldFeedTitle, event.newFeedTitle);
            }
        });

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
