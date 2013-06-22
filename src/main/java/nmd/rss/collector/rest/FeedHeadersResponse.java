package nmd.rss.collector.rest;

import java.util.List;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.06.13
 */
class FeedHeadersResponse extends SuccessResponse {

    private List<FeedHeaderHelper> helpers;

    private FeedHeadersResponse() {
        super();

        this.helpers = null;
    }

    List<FeedHeaderHelper> getHelpers() {
        return this.helpers;
    }

    void setHelpers(final List<FeedHeaderHelper> helpers) {
        this.helpers = helpers;
    }

}
