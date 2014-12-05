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

    private static final Type CATEGORY_IMPORT_CONTEXT_HELPER_LIST_TYPE = new TypeToken<ArrayList<CategoryImportContextHelper>>() {
    }.getType();

    private String name;
    private String contexts;
    private String status;
    private String id;

    public static String convert(final List<CategoryImportContext> contexts) {
        guard(notNull(contexts));

        final List<CategoryImportContextHelper> helpers = new ArrayList<>();

        for (final CategoryImportContext context : contexts) {
            helpers.add(CategoryImportContextHelper.convert(context));
        }

        return GSON.toJson(helpers);
    }

    public static List<CategoryImportContext> convert(final String data) {
        guard(notNull(data));

        final List<CategoryImportContext> result = new ArrayList<>();

        final List<CategoryImportContextHelper> helpers = GSON.fromJson(data, CATEGORY_IMPORT_CONTEXT_HELPER_LIST_TYPE);

        for (final CategoryImportContextHelper helper : helpers) {
            result.add(CategoryImportContextHelper.convert(helper));
        }

        return result;
    }

    private static CategoryImportContextHelper convert(final CategoryImportContext context) {

        final CategoryImportContextHelper helper = new CategoryImportContextHelper();

        helper.name = context.getCategoryName();
        helper.contexts = FeedImportContextHelper.convert(context.getFeedImportContexts());
        helper.status = context.getStatus().toString();
        helper.id = context.getCategoryId();

        return helper;
    }

    private static CategoryImportContext convert(final CategoryImportContextHelper helper) {
        final String categoryName = helper.name;
        final CategoryImportTaskStatus status = CategoryImportTaskStatus.valueOf(helper.status);
        final String categoryId = helper.id;
        final List<FeedImportContext> feedImportContexts = FeedImportContextHelper.convert(helper.contexts);

        return new CategoryImportContext(categoryName, feedImportContexts, status, categoryId);
    }

}
