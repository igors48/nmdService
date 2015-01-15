package nmd.orb.http.wrappers;

import nmd.orb.http.tools.ResponseBody;
import nmd.orb.services.filter.FeedItemReportFilter;

import java.util.UUID;

/**
 * @author : igu
 */
public interface ReadsServiceWrapper {

    ResponseBody getFeedsReadReport();

    ResponseBody markItemAsRead(UUID feedId, String itemId);

    ResponseBody markAllItemsAsRead(UUID feedId, long topItemTimestamp);

    ResponseBody toggleItemAsReadLater(UUID feedId, String itemId);

    ResponseBody getFeedItemsReport(UUID feedId, FeedItemReportFilter filter);

    ResponseBody getFeedItemsCardsReport(UUID feedId, int offset, int size);

}
