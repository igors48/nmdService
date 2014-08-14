package nmd.rss.collector.rest.wrappers;

import nmd.rss.collector.rest.tools.ResponseBody;

import java.util.UUID;

/**
 * @author : igu
 */
public interface ReadsServiceInterface {

    ResponseBody getFeedsReadReport();

    ResponseBody markItemAsRead(UUID feedId, String itemId);

    ResponseBody markAllItemsAsRead(UUID feedId);

    ResponseBody toggleItemAsReadLater(UUID feedId, String itemId);

    ResponseBody getFeedItemsReport(UUID feedId);

    ResponseBody getFeedItemsCardsReport(UUID feedId, int offset, int size);

}
