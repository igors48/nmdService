package nmd.orb.gae.repositories.converters.helpers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import nmd.orb.services.importer.CategoryImportContext;
import nmd.orb.services.importer.CategoryImportTaskStatus;
import nmd.orb.services.importer.FeedImportContext;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * @author : igu
 */
public class CategoryImportContextHelper {

    private static final Gson GSON = new Gson();

    private static final Type FEED_IMPORT_CONTEXT_HELPER_LIST_TYPE = new TypeToken<ArrayList<FeedImportContextHelper>>() {
    }.getType();

    private String name;
    private String contexts;
    private String status;
    private String id;

    public static CategoryImportContextHelper convert(final CategoryImportContext context) {
        guard(notNull(context));

        final CategoryImportContextHelper helper = new CategoryImportContextHelper();

        helper.name = context.getCategoryName();
        helper.contexts = convert(context.getFeedImportContexts());
        helper.status = context.getStatus().toString();
        helper.id = context.getCategoryId();

        return helper;
    }

    public static CategoryImportContext convert(final CategoryImportContextHelper helper) {
        guard(notNull(helper));

        final String categoryName = helper.name;
        final List<FeedImportContext> feedImportContexts = GSON.fromJson(helper.contexts, FEED_IMPORT_CONTEXT_HELPER_LIST_TYPE);
        final CategoryImportTaskStatus status = CategoryImportTaskStatus.valueOf(helper.status);
        final String categoryId = helper.id;

        return new CategoryImportContext(categoryName, feedImportContexts, status, categoryId);
    }

    private static String convert(final List<FeedImportContext> contexts) {
        guard(notNull(contexts));

        final List<FeedImportContextHelper> helpers = new ArrayList<>();

        for (final FeedImportContext current : contexts) {
            helpers.add(FeedImportContextHelper.convert(current));
        }

        return GSON.toJson(helpers);
    }

}
