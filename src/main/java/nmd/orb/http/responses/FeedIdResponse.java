package nmd.orb.http.responses;

import java.util.UUID;

import static nmd.orb.collector.feed.FeedHeader.isValidFeedHeaderId;
import static nmd.orb.util.Assert.guard;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.06.13
 */
public class FeedIdResponse extends SuccessResponse {

    public UUID feedId;

    private FeedIdResponse() {
        // empty
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        FeedIdResponse that = (FeedIdResponse) o;

        if (feedId != null ? !feedId.equals(that.feedId) : that.feedId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (feedId != null ? feedId.hashCode() : 0);
        return result;
    }

    public static FeedIdResponse create(final UUID feedId) {
        guard(isValidFeedHeaderId(feedId));

        final FeedIdResponse result = new FeedIdResponse();

        result.feedId = feedId;

        return result;
    }

}
