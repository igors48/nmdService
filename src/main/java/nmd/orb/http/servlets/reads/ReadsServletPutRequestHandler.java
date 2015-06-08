package nmd.orb.http.servlets.reads;

import nmd.orb.http.Handler;
import nmd.orb.http.tools.FeedAndItemIds;
import nmd.orb.http.tools.ResponseBody;
import nmd.orb.http.wrappers.ReadsServiceWrapper;

import java.util.List;
import java.util.Map;

import static nmd.orb.error.ServiceError.invalidFeedOrItemId;
import static nmd.orb.error.ServiceError.invalidMarkMode;
import static nmd.orb.http.tools.ResponseBody.createErrorJsonResponse;
import static nmd.orb.http.tools.ServletTools.parseFeedAndItemIds;
import static nmd.orb.http.tools.ServletTools.parseLong;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * @author : igu
 */
public class ReadsServletPutRequestHandler implements Handler {

    private static final String MARK_AS_PARAMETER_NAME = "markAs";
    private static final String MARK_AS_READ = "read";
    private static final String MARK_AS_NOT_READ = "notRead";
    private static final String MARK_AS_READ_LATER = "readLater";

    private final ReadsServiceWrapper readsService;

    public ReadsServletPutRequestHandler(final ReadsServiceWrapper readsService) {
        guard(notNull(readsService));
        this.readsService = readsService;
    }

    //PUT /{feedId}/{itemId}&markAs=read|readLater|notRead -- mark item as read or read later
    //PUT /{feedId}?topItemTimestamp=timestamp -- mark all items as read before given timestamp
    @Override
    public ResponseBody handle(final List<String> elements, final Map<String, String> parameters, final String body) {
        guard(notNull(elements));
        guard(notNull(parameters));
        guard(notNull(body));

        final FeedAndItemIds feedAndItemIds = parseFeedAndItemIds(elements);

        if (feedAndItemIds == null) {
            return createErrorJsonResponse(invalidFeedOrItemId());
        }

        if (feedAndItemIds.itemId.isEmpty()) {
            final Long topItemTimestamp = parseLong(parameters.get("topItemTimestamp"));

            return this.readsService.markAllItemsAsRead(feedAndItemIds.feedId, topItemTimestamp == null ? 0 : topItemTimestamp);
        }

        final String markMode = parameters.get(MARK_AS_PARAMETER_NAME);

        if (markMode == null) {
            return createErrorJsonResponse(invalidMarkMode(null));
        }

        switch (markMode) {
            case MARK_AS_READ: {
                return this.readsService.markItemAsRead(feedAndItemIds.feedId, feedAndItemIds.itemId);
            }
            case MARK_AS_NOT_READ: {
                return this.readsService.markItemAsNotRead(feedAndItemIds.feedId, feedAndItemIds.itemId);
            }
            case MARK_AS_READ_LATER: {
                return this.readsService.toggleItemAsReadLater(feedAndItemIds.feedId, feedAndItemIds.itemId);
            }
            default: {
                return createErrorJsonResponse(invalidMarkMode(markMode));
            }
        }
    }

}
