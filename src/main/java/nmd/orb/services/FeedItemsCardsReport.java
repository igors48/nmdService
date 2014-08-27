package nmd.orb.services;

import java.util.List;
import java.util.UUID;

import static nmd.orb.feed.FeedHeader.isValidFeedHeaderId;
import static nmd.orb.feed.FeedHeader.isValidFeedHeaderTitle;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 05.08.2014
 */
public class FeedItemsCardsReport {

    public final UUID feedId;
    public final String title;
    public final boolean first;
    public final boolean last;
    public final List<FeedItemReport> reports;

    public FeedItemsCardsReport(final UUID feedId, final String title, final boolean first, final boolean last, final List<FeedItemReport> reports) {
        guard(isValidFeedHeaderId(feedId));
        this.feedId = feedId;

        guard(isValidFeedHeaderTitle(title));
        this.title = title;

        this.first = first;
        this.last = last;

        guard(notNull(reports));
        this.reports = reports;
    }

}
