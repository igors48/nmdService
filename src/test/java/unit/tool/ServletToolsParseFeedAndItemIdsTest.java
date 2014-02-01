package unit.tool;

import nmd.rss.collector.rest.FeedAndItemIds;
import nmd.rss.collector.rest.ServletTools;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * User: igu
 * Date: 28.11.13
 */
public class ServletToolsParseFeedAndItemIdsTest {

    private static final UUID FEED_UUID = UUID.randomUUID();
    private static final String FEED_ID = FEED_UUID.toString();
    private static final String ITEM_ID = "48";

    @Test
    public void whenAllOkThenBothIdReturn() {
        final FeedAndItemIds feedAndItemIds = createAndParse(FEED_ID, ITEM_ID, "");

        assertEquals(FEED_UUID, feedAndItemIds.feedId);
        assertEquals(ITEM_ID, feedAndItemIds.itemId);
    }

    @Test
    public void whenFeedIdIsAbsentThenNullReturns() {
        final FeedAndItemIds feedAndItemIds = createAndParse("", ITEM_ID, "");

        assertNull(feedAndItemIds);
    }

    @Test
    public void whenFeedIdCanNotBeParsedThenNullReturns() {
        final FeedAndItemIds feedAndItemIds = createAndParse("trash", ITEM_ID, "");

        assertNull(feedAndItemIds);
    }

    @Test
    public void whenItemIdIsAbsentThenEmptyIdReturns() {
        final FeedAndItemIds feedAndItemIds = createAndParse(FEED_ID, "", "");

        assertEquals("", feedAndItemIds.itemId);
    }

    @Test
    public void whenItemIdContainsOnlySpacesThenEmptyIdReturns() {
        final FeedAndItemIds feedAndItemIds = createAndParse(FEED_ID, "  ", "");

        assertEquals("", feedAndItemIds.itemId);
    }

    @Test
    public void whenPathInfoIsEmptyThenNullReturns() {
        assertNull(ServletTools.parseFeedAndItemIds(""));
    }

    @Test
    public void whenPathInfoIsNullThenNullReturns() {
        assertNull(ServletTools.parseFeedAndItemIds(null));
    }

    @Test
    public void whenThereIsAdditionalInfoAfterIdsThenItIsIgnored() {
        final FeedAndItemIds feedAndItemIds = createAndParse(FEED_ID, ITEM_ID, "postfix");

        assertEquals(FEED_UUID, feedAndItemIds.feedId);
        assertEquals(ITEM_ID, feedAndItemIds.itemId);
    }

    private static FeedAndItemIds createAndParse(final String feedId, final String itemId, final String postfix) {
        final String pathInfo = createPathInfo(feedId, itemId, postfix);

        return ServletTools.parseFeedAndItemIds(pathInfo);
    }

    private static String createPathInfo(final String feedId, final String itemId, final String postfix) {
        final String pathInfo = "/" + feedId + "/" + itemId;

        return postfix.isEmpty() ? pathInfo : pathInfo + "/" + postfix;
    }

}
