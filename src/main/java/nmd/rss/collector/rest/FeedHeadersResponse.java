package nmd.rss.collector.rest;

import nmd.rss.collector.feed.FeedHeader;

import java.util.ArrayList;
import java.util.List;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.06.13
 */
class FeedHeadersResponse extends SuccessResponse {

    private List<FeedHeaderHelper> headers;

    private FeedHeadersResponse() {
        super();

        this.headers = null;
    }

    private List<FeedHeaderHelper> getHeaders() {
        return this.headers;
    }

    private void setHeaders(final List<FeedHeaderHelper> headers) {
        this.headers = headers;
    }

    static FeedHeadersResponse convert(final List<FeedHeader> headers) {
        assertNotNull(headers);

        final List<FeedHeaderHelper> helpers = new ArrayList<>();

        for (final FeedHeader header : headers) {
            final FeedHeaderHelper helper = FeedHeaderHelper.convert(header);

            helpers.add(helper);
        }

        final FeedHeadersResponse result = new FeedHeadersResponse();
        result.setHeaders(helpers);

        return result;
    }

}
