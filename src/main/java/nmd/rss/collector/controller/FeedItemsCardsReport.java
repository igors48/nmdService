package nmd.rss.collector.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static nmd.rss.collector.feed.FeedHeader.isValidFeedHeaderId;
import static nmd.rss.collector.feed.FeedHeader.isValidFeedHeaderTitle;
import static nmd.rss.collector.util.Assert.guard;
import static nmd.rss.collector.util.Parameter.notNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 05.08.2014
 */
public class FeedItemsCardsReport {

    public final UUID feedId;
    public final String title;
    public final boolean first;
    public final boolean last;
    public final List<FeedItemReport> feedItemsCardsReports;

    public FeedItemsCardsReport(final UUID feedId, final String title, final boolean first, final boolean last, final List<FeedItemReport> feedItemsCardsReports) {
        guard(isValidFeedHeaderId(feedId));
        this.feedId = feedId;

        guard(isValidFeedHeaderTitle(title));
        this.title = title;

        this.first = first;
        this.last = last;

        guard(notNull(feedItemsCardsReports));
        this.feedItemsCardsReports = feedItemsCardsReports;
    }

    public FeedItemsCardsReport(final UUID feedId, final String title) {
        this(feedId, title, true, true, new ArrayList<FeedItemReport>());
    }

}
