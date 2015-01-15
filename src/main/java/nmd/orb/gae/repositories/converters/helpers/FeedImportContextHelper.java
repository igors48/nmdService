package nmd.orb.gae.repositories.converters.helpers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import nmd.orb.services.importer.FeedImportContext;
import nmd.orb.services.importer.FeedImportTaskStatus;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * @author : igu
 */
public class FeedImportContextHelper {

    private static final Gson GSON = new Gson();

    private static final Type FEED_IMPORT_CONTEXT_HELPER_LIST_TYPE = new TypeToken<ArrayList<FeedImportContextHelper>>() {
    }.getType();

    private String link;
    private String title;
    private int tries;
    private String status;

    public static List<FeedImportContext> convert(final String data) {
        guard(notNull(data));

        final List<FeedImportContext> result = new ArrayList<>();

        final List<FeedImportContextHelper> helpers = GSON.fromJson(data, FEED_IMPORT_CONTEXT_HELPER_LIST_TYPE);

        for (final FeedImportContextHelper helper : helpers) {
            result.add(FeedImportContextHelper.convert(helper));
        }

        return result;
    }

    public static String convert(final List<FeedImportContext> contexts) {
        guard(notNull(contexts));

        final List<FeedImportContextHelper> helpers = new ArrayList<>();

        for (final FeedImportContext current : contexts) {
            helpers.add(FeedImportContextHelper.convert(current));
        }

        return GSON.toJson(helpers);
    }

    private static FeedImportContextHelper convert(final FeedImportContext context) {
        final FeedImportContextHelper helper = new FeedImportContextHelper();

        helper.link = context.getFeedLink();
        helper.title = context.getFeedTitle();
        helper.tries = context.getTriesLeft();
        helper.status = context.getStatus().toString();

        return helper;
    }

    private static FeedImportContext convert(final FeedImportContextHelper helper) {
        final String feedLink = helper.link;
        final String feedTitle = helper.title;
        final int triesLeft = helper.tries;
        final FeedImportTaskStatus status = FeedImportTaskStatus.valueOf(helper.status);

        return new FeedImportContext(feedLink, feedTitle, triesLeft, status);
    }

}
