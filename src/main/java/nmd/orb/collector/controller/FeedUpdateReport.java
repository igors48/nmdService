package nmd.orb.collector.controller;

import nmd.orb.feed.FeedItemsMergeReport;

import java.util.UUID;

import static nmd.orb.util.Assert.assertNotNull;
import static nmd.orb.util.Assert.assertValidUrl;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 30.06.13
 */
public class FeedUpdateReport {

    public final String feedLink;
    public final UUID feedId;
    public final FeedItemsMergeReport mergeReport;

    public FeedUpdateReport(final String feedLink, final UUID feedId, final FeedItemsMergeReport mergeReport) {
        assertValidUrl(feedLink);
        this.feedLink = feedLink;

        assertNotNull(feedId);
        this.feedId = feedId;

        assertNotNull(mergeReport);
        this.mergeReport = mergeReport;
    }

}
