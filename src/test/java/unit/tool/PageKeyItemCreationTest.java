package unit.tool;

import nmd.orb.feed.FeedItem;
import nmd.orb.util.Direction;
import nmd.orb.util.Page;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static unit.feed.controller.AbstractControllerTestBase.create;

/**
 * Created by igor on 06.02.2015.
 */
public class PageKeyItemCreationTest {

    private static final FeedItem[] FEED_ITEMS = {
            create(1),
            create(2),
            create(3),
            create(4),
            create(5)
    };

    private List<FeedItem> items;

    @Before
    public void setUp() {
        this.items = Arrays.asList(FEED_ITEMS);
    }

    @Test
    public void whenItemsListIsEmptyThenEmptyPageReturns() {
        final Page<FeedItem> page = Page.create(new ArrayList<FeedItem>(), "48", 3, Direction.NEXT);

        assertTrue(page.first);
        assertTrue(page.last);
        assertTrue(page.items.isEmpty());
    }

    @Test
    public void whenItemsListIsEmptyAndKeyItemNotDefinedThenEmptyPageReturns() {
        final Page<FeedItem> page = Page.create(new ArrayList<FeedItem>(), "", 3, Direction.NEXT);

        assertTrue(page.first);
        assertTrue(page.last);
        assertTrue(page.items.isEmpty());
    }

    @Test
    public void whenKeyItemNotSetThenFirstItemIsUsed() {
        final Page<FeedItem> page = Page.create(this.items, "", 3, Direction.NEXT);

        assertEquals(create(1), page.items.get(0));
    }

    @Test
    public void whenKeyItemNotFoundInListThenEmptyPageReturns() {
        final Page<FeedItem> page = Page.create(this.items, "48", 3, Direction.NEXT);

        assertTrue(page.first);
        assertTrue(page.last);
        assertTrue(page.items.isEmpty());
    }

    @Test
    public void whenKeyItemIsFirstItemInListThenItIsFirstPage() {
        final Page<FeedItem> page = Page.create(this.items, "1", 3, Direction.NEXT);

        assertTrue(page.first);
        assertFalse(page.last);
    }

    @Test
    public void whenKeyItemIsLastItemInListThenItIsFirstPage() {
        final Page<FeedItem> page = Page.create(this.items, "5", 3, Direction.NEXT);

        assertFalse(page.first);
        assertTrue(page.last);
    }

    @Test
    public void happyFlowForward() {
        final Page<FeedItem> page = Page.create(this.items, "3", 2, Direction.NEXT);

        assertFalse(page.first);
        assertFalse(page.last);

        assertEquals(3, page.items.size());

        assertFalse(page.items.contains(create(1)));
        assertFalse(page.items.contains(create(2)));
        assertTrue(page.items.contains(create(3)));
        assertTrue(page.items.contains(create(4)));
        assertTrue(page.items.contains(create(5)));
    }

    @Test
    public void happyFlowBackward() {
        final Page<FeedItem> page = Page.create(this.items, "3", 2, Direction.PREVIOUS);

        assertFalse(page.first);
        assertFalse(page.last);

        assertEquals(3, page.items.size());

        assertTrue(page.items.contains(create(1)));
        assertTrue(page.items.contains(create(2)));
        assertTrue(page.items.contains(create(3)));
        assertFalse(page.items.contains(create(4)));
        assertFalse(page.items.contains(create(5)));
    }

    @Test
    public void whenThereAreNoEnoughBackwardItemsThenListIsCut() {
        final Page<FeedItem> page = Page.create(this.items, "3", 5, Direction.PREVIOUS);

        assertFalse(page.first);
        assertFalse(page.last);

        assertEquals(3, page.items.size());

        assertTrue(page.items.contains(create(1)));
        assertTrue(page.items.contains(create(2)));
        assertTrue(page.items.contains(create(3)));
        assertFalse(page.items.contains(create(4)));
        assertFalse(page.items.contains(create(5)));
    }

    @Test
    public void whenThereAreNoEnoughForwardItemsThenListIsCut() {
        final Page<FeedItem> page = Page.create(this.items, "3", 5, Direction.NEXT);

        assertFalse(page.first);
        assertFalse(page.last);

        assertEquals(3, page.items.size());

        assertFalse(page.items.contains(create(1)));
        assertFalse(page.items.contains(create(2)));
        assertTrue(page.items.contains(create(3)));
        assertTrue(page.items.contains(create(4)));
        assertTrue(page.items.contains(create(5)));
    }

}
