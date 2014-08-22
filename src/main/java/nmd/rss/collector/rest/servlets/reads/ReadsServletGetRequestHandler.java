package nmd.rss.collector.rest.servlets.reads;

import nmd.rss.collector.rest.Handler;
import nmd.rss.collector.rest.tools.ResponseBody;
import nmd.rss.collector.rest.wrappers.ReadsServiceWrapper;
import nmd.rss.collector.rest.wrappers.ReadsServiceWrapperImpl;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static nmd.rss.collector.error.ServiceError.invalidFeedId;
import static nmd.rss.collector.error.ServiceError.invalidOffsetOrSize;
import static nmd.rss.collector.feed.FeedHeader.isValidFeedHeaderId;
import static nmd.rss.collector.rest.tools.ResponseBody.createErrorJsonResponse;
import static nmd.rss.collector.rest.tools.ServletTools.parseInteger;
import static nmd.rss.collector.rest.tools.ServletTools.parseUuid;
import static nmd.rss.collector.util.Assert.guard;
import static nmd.rss.collector.util.Parameter.isPositive;
import static nmd.rss.collector.util.Parameter.notNull;

/**
 * @author : igu
 */
public class ReadsServletGetRequestHandler implements Handler {

    public static final ReadsServletGetRequestHandler READS_SERVLET_GET_REQUEST_HANDLER = new ReadsServletGetRequestHandler(ReadsServiceWrapperImpl.READS_SERVICE_WRAPPER);

    private final ReadsServiceWrapper readsService;

    public ReadsServletGetRequestHandler(final ReadsServiceWrapper readsService) {
        guard(notNull(readsService));
        this.readsService = readsService;
    }

    //GET -- reads report
    //GET /{feedId} -- feed items report
    //GET /{feedId}?offset={offset}&size={size} -- feed items cards report
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

        if (parameters.isEmpty()) {
            return isValidFeedHeaderId(feedId) ? this.readsService.getFeedItemsReport(feedId) : createErrorJsonResponse(invalidFeedId(element));
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
