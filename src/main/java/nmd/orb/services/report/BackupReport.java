package nmd.orb.services.report;

import nmd.orb.feed.FeedHeader;
import nmd.orb.reader.Category;
import nmd.orb.reader.ReadFeedItems;

import java.util.Map;
import java.util.Set;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Created by igor on 23.11.2014.
 */
public class BackupReport {

    public final Map<Category, Set<FeedHeader>> map;
    public final Set<ReadFeedItems> lostLinks;
    public final Set<FeedHeader> lostHeaders;

    public BackupReport(final Map<Category, Set<FeedHeader>> map, final Set<ReadFeedItems> lostLinks, final Set<FeedHeader> lostHeaders) {
        guard(notNull(map));
        this.map = map;

        guard(notNull(lostLinks));
        this.lostLinks = lostLinks;

        guard(notNull(lostHeaders));
        this.lostHeaders = lostHeaders;
    }

}
