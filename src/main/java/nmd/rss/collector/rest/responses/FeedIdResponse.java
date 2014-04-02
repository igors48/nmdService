package nmd.rss.collector.rest.responses;

import java.util.UUID;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.06.13
 */
public class FeedIdResponse extends SuccessResponse {

    public UUID feedId = null;

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
        assertNotNull(feedId);

        final FeedIdResponse result = new FeedIdResponse();

        result.feedId = feedId;

        return result;
    }

}
