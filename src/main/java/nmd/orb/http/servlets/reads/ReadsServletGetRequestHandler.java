package nmd.orb.http.servlets.reads;

import nmd.orb.error.ServiceError;
import nmd.orb.http.Handler;
import nmd.orb.http.tools.ResponseBody;
import nmd.orb.http.wrappers.ReadsServiceWrapper;
import nmd.orb.services.filter.FeedItemReportFilter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static nmd.orb.error.ServiceError.invalidFeedId;
import static nmd.orb.error.ServiceError.invalidOffsetOrSize;
import static nmd.orb.error.ServiceError.invalidParametersCount;
import static nmd.orb.feed.FeedHeader.isValidFeedHeaderId;
import static nmd.orb.http.tools.ResponseBody.createErrorJsonResponse;
import static nmd.orb.http.tools.ResponseBody.createJsonResponse;
import static nmd.orb.http.tools.ServletTools.parseInteger;
import static nmd.orb.http.tools.ServletTools.parseUuid;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.isPositive;
import static nmd.orb.util.Parameter.notNull;

/**
 * @author : igu
 */
public class ReadsServletGetRequestHandler implements Handler {

    private final ReadsServiceWrapper readsService;

    public ReadsServletGetRequestHandler(final ReadsServiceWrapper readsService) {
        guard(notNull(readsService));
        this.readsService = readsService;
    }

    //GET -- reads report
    //GET /{feedId}?filter={filterName} -- feed items report
    //GET /{feedId}?offset={offset}&size={size} -- feed items cards report
    //GET /{feedId}/{itemId}/{next|prev}/{size} -- feed items cards report
    @Override
    public ResponseBody handle(final List<String> elements, final Map<String, String> parameters, final String body) {
        guard(notNull(elements));
        guard(notNull(parameters));
        guard(notNull(body));

        if (elements.isEmpty()) {
            return this.readsService.getFeedsReadReport();
        }

        final String element = elements.get(0);
        final UUID feedId = parseUuid(element);

        if (elements.size() == 1) {

            if (parameters.isEmpty()) {
                return isValidFeedHeaderId(feedId) ? this.readsService.getFeedItemsReport(feedId, FeedItemReportFilter.SHOW_ALL) : createErrorJsonResponse(invalidFeedId(element));
            }

            final String filterName = parameters.get("filter");

            if (filterName != null) {
                final FeedItemReportFilter filter = FeedItemReportFilter.forName(filterName);

                return isValidFeedHeaderId(feedId) ? this.readsService.getFeedItemsReport(feedId, filter) : createErrorJsonResponse(invalidFeedId(element));
            }
        }

        if (elements.size() < 4) {
            return createErrorJsonResponse(invalidParametersCount());
        }

        final String offsetAsString = parameters.get("offset");
        final Integer offset = parseInteger(offsetAsString);

        final String sizeAsString = parameters.get("size");
        final Integer size = parseInteger(sizeAsString);

        if (offset == null || size == null) {
            return createErrorJsonResponse(invalidOffsetOrSize(offsetAsString, sizeAsString));
        }

        if (!(isPositive(offset) && isPositive(size))) {
            return createErrorJsonResponse(invalidOffsetOrSize(offsetAsString, sizeAsString));
        }

        return isValidFeedHeaderId(feedId) ? this.readsService.getFeedItemsCardsReport(feedId, offset, size) : createErrorJsonResponse(invalidFeedId(element));
    }
}
