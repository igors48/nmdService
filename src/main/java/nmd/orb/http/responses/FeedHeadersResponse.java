package nmd.orb.http.responses;

import nmd.orb.collector.feed.FeedHeader;
import nmd.orb.http.responses.payload.FeedHeaderPayload;

import java.util.ArrayList;
import java.util.List;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.06.13
 */
public class FeedHeadersResponse extends SuccessResponse {

    public List<FeedHeaderPayload> headers;

    private FeedHeadersResponse() {
        // empty
    }

    public static FeedHeadersResponse convert(final List<FeedHeader> headers) {
        guard(notNull(headers));

        final List<FeedHeaderPayload> payload = new ArrayList<>();

        for (final FeedHeader header : headers) {
            final FeedHeaderPayload feedHeaderPayload = FeedHeaderPayload.create(header);

            payload.add(feedHeaderPayload);
        }

        final FeedHeadersResponse result = new FeedHeadersResponse();
        result.headers = payload;

        return result;
    }

}
