package nmd.rss.collector.rest.responses;

import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.collector.rest.responses.payload.FeedHeaderPayload;

import java.util.ArrayList;
import java.util.List;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.06.13
 */
public class FeedHeadersResponse extends SuccessResponse {

    private List<FeedHeaderPayload> headers = null;

    private FeedHeadersResponse() {
        // empty
    }

    public List<FeedHeaderPayload> getHeaders() {
        return this.headers;
    }

    public static FeedHeadersResponse convert(final List<FeedHeader> headers) {
        assertNotNull(headers);

        final List<FeedHeaderPayload> payload = new ArrayList<>();

        for (final FeedHeader header : headers) {
            final FeedHeaderPayload feedHeaderPayload = FeedHeaderPayload.convert(header);

            payload.add(feedHeaderPayload);
        }

        final FeedHeadersResponse result = new FeedHeadersResponse();
        result.headers = payload;

        return result;
    }

}
