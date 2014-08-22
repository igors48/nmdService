package nmd.rss.http.wrappers;

import nmd.rss.http.tools.ResponseBody;

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
