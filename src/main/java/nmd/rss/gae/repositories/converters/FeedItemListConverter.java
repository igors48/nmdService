package nmd.rss.gae.repositories.converters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import nmd.rss.collector.feed.FeedItem;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.collector.util.Assert.assertStringIsValid;

/**
 * User: igu
 * Date: 21.10.13
 */
public class FeedItemListConverter {

    private static final Gson GSON = new Gson();

    private static final Type FEED_ITEM_HELPER_LIST_TYPE = new TypeToken<ArrayList<FeedItemHelper>>() {
    }.getType();

    public static List<FeedItem> convert(final String data) {
        assertStringIsValid(data);

        final List<FeedItem> result = new ArrayList<>();

        final List<FeedItemHelper> helpers = GSON.fromJson(data, FEED_ITEM_HELPER_LIST_TYPE);

        for (final FeedItemHelper helper : helpers) {
            final FeedItem item = FeedItemHelper.convert(helper);

            result.add(item);
        }

        return result;
    }

    public static String convert(final List<FeedItem> feedItems) {
        assertNotNull(feedItems);

        final List<FeedItemHelper> helpers = new ArrayList<>();

        for (final FeedItem current : feedItems) {
            helpers.add(FeedItemHelper.convert(current));
        }

        return GSON.toJson(helpers);
    }

}
