package unit.tool;

import nmd.rss.http.tools.FeedAndItemIds;
import nmd.rss.http.tools.ServletTools;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
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
        final FeedAndItemIds feedAndItemIds = createAndParse(FEED_ID, "", "");

        assertEquals("", feedAndItemIds.itemId);
    }

    @Test
    public void whenPathInfoIsEmptyThenNullReturns() {
        assertNull(ServletTools.parseFeedAndItemIds(new ArrayList<String>()));
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
        final List<String> elements = createPathInfo(feedId, itemId, postfix);

        return ServletTools.parseFeedAndItemIds(elements);
    }

    private static List<String> createPathInfo(final String feedId, final String itemId, final String postfix) {
        final List<String> pathInfo = new ArrayList<>();

        pathInfo.add(feedId);
        pathInfo.add(itemId);
        pathInfo.add(postfix);

        return pathInfo;
    }

}
