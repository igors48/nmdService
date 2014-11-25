package nmd.orb.http.responses.payload;

import nmd.orb.feed.FeedHeader;
import nmd.orb.reader.Category;

import java.util.HashSet;
import java.util.Set;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Created by igor on 25.11.2014.
 */
public class BackupReportPayload {

    public CategoryPayload category;
    public Set<FeedHeaderPayload> feeds;

    private BackupReportPayload() {
        // empty
    }

    public static BackupReportPayload create(Category category, Set<FeedHeader> headers) {
        guard(notNull(category));
        guard(notNull(headers));

        final BackupReportPayload payload = new BackupReportPayload();

        payload.category = CategoryPayload.create(category);
        payload.feeds = new HashSet<>();

        for (final FeedHeader header : headers) {
            final FeedHeaderPayload feedHeaderPayload = FeedHeaderPayload.create(header);
            payload.feeds.add(feedHeaderPayload);
        }

        return payload;
    }

}
