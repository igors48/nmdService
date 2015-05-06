package nmd.orb.services.update;

import static nmd.orb.collector.scheduler.FeedUpdateTask.isValidMaxFeedItemsCount;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.isValidUrl;

/**
 * @author : igu
 */
public class FeedLinkAndMaxFeedItemsCount {

    public final String feedLink;
    public final int maxFeedItemsCount;

    public FeedLinkAndMaxFeedItemsCount(final String feedLink, final int maxFeedItemsCount) {
        guard(isValidUrl(feedLink));
        this.feedLink = feedLink;

        guard(isValidMaxFeedItemsCount(maxFeedItemsCount));
        this.maxFeedItemsCount = maxFeedItemsCount;
    }

}
