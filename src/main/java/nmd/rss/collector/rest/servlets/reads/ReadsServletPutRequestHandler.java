package nmd.rss.collector.rest.servlets.reads;

import nmd.rss.collector.rest.Handler;
import nmd.rss.collector.rest.tools.FeedAndItemIds;
import nmd.rss.collector.rest.tools.ResponseBody;
import nmd.rss.collector.rest.wrappers.ReadsServiceWrapper;
import nmd.rss.collector.rest.wrappers.ReadsServiceWrapperImpl;

import java.util.List;
import java.util.Map;

import static nmd.rss.collector.error.ServiceError.invalidFeedOrItemId;
import static nmd.rss.collector.error.ServiceError.invalidMarkMode;
import static nmd.rss.collector.rest.tools.ResponseBody.createErrorJsonResponse;
import static nmd.rss.collector.rest.tools.ServletTools.parseFeedAndItemIds;
import static nmd.rss.collector.util.Assert.guard;
import static nmd.rss.collector.util.Parameter.notNull;

/**
 * @author : igu
 */
public class ReadsServletPutRequestHandler implements Handler {

    public static final ReadsServletPutRequestHandler READS_SERVLET_PUT_REQUEST_HANDLER = new ReadsServletPutRequestHandler(ReadsServiceWrapperImpl.READS_SERVICE_WRAPPER);

    private static final String MARK_AS_PARAMETER_NAME = "markAs";
    private static final String MARK_AS_READ = "read";
    private static final String MARK_AS_READ_LATER = "readLater";

    private final ReadsServiceWrapper readsService;

    public ReadsServletPutRequestHandler(final ReadsServiceWrapper readsService) {
        guard(notNull(readsService));
        this.readsService = readsService;
    }

    //PUT /{feedId}/{itemId}&mark-as=read|read-later -- mark item as read or read later
    //PUT /{feedId} -- mark all items as read
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
            return this.readsService.markAllItemsAsRead(feedAndItemIds.feedId);
        }

        final String markMode = parameters.get(MARK_AS_PARAMETER_NAME);

        if (!(MARK_AS_READ.equals(markMode) || MARK_AS_READ_LATER.equals(markMode))) {
            return createErrorJsonResponse(invalidMarkMode(markMode));
        }

        return markMode.equals(MARK_AS_READ) ? this.readsService.markItemAsRead(feedAndItemIds.feedId, feedAndItemIds.itemId) : this.readsService.toggleItemAsReadLater(feedAndItemIds.feedId, feedAndItemIds.itemId);
    }
}
