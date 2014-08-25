package nmd.orb.http.wrappers;

import nmd.orb.http.tools.ResponseBody;

import java.util.UUID;

/**
 * @author : igu
 */
public interface ReadsServiceWrapper {

    ResponseBody getFeedsReadReport();

    ResponseBody markItemAsRead(UUID feedId, String itemId);

    ResponseBody markAllItemsAsRead(UUID feedId);

    ResponseBody toggleItemAsReadLater(UUID feedId, String itemId);

    ResponseBody getFeedItemsReport(UUID feedId);

    ResponseBody getFeedItemsCardsReport(UUID feedId, int offset, int size);

}
