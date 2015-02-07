package nmd.orb.http.servlets.reads;

import nmd.orb.http.Handler;
import nmd.orb.http.tools.ResponseBody;
import nmd.orb.http.wrappers.ReadsServiceWrapper;
import nmd.orb.services.filter.FeedItemReportFilter;
import nmd.orb.util.Direction;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static nmd.orb.error.ServiceError.*;
import static nmd.orb.feed.FeedHeader.isValidFeedHeaderId;
import static nmd.orb.feed.FeedItem.isValidFeedItemGuid;
import static nmd.orb.http.tools.ResponseBody.createErrorJsonResponse;
import static nmd.orb.http.tools.ServletTools.parseInteger;
import static nmd.orb.http.tools.ServletTools.parseUuid;
import static nmd.orb.util.Assert.guard;
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
    //GET /{feedId}/{itemId}/{next|prev}/{size} -- feed items cards report
    @Override
    public ResponseBody handle(final List<String> elements, final Map<String, String> parameters, final String body) {
        guard(notNull(elements));
        guard(notNull(parameters));
        guard(notNull(body));

        if (elements.isEmpty()) {
            return this.readsService.getFeedsReadReport();
        }

        final String feedIdAsString = elements.get(0);
        final UUID feedId = parseUuid(feedIdAsString);

        if (!isValidFeedHeaderId(feedId)) {
            return createErrorJsonResponse(invalidFeedId(feedIdAsString));
        }

        if (elements.size() == 1) {
            final String filterName = parameters.get("filter");
            final FeedItemReportFilter filter = filterName == null ? FeedItemReportFilter.SHOW_ALL : FeedItemReportFilter.forName(filterName);

            return this.readsService.getFeedItemsReport(feedId, filter);
        }

        if (elements.size() < 4) {
            return createErrorJsonResponse(invalidParametersCount());
        }

        final String itemIdAsString = elements.get(1);

        if (!isValidFeedItemGuid(itemIdAsString)) {
            return createErrorJsonResponse(invalidItemId(itemIdAsString));
        }

        final String directionAsString = elements.get(2);
        final Direction direction = Direction.forName(directionAsString);

        if (direction == null) {
            return createErrorJsonResponse(invalidDirection(directionAsString));
        }

        final String sizeAsString = elements.get(3);
        final Integer size = parseInteger(sizeAsString);

        if (size == null) {
            return createErrorJsonResponse(invalidSize(sizeAsString));
        }

        return this.readsService.getFeedItemsCardsReport(feedId, itemIdAsString, size, direction);
    }

}
