package unit.tool;

import nmd.orb.feed.FeedItem;
import nmd.orb.util.Page;
import org.junit.Before;
import org.junit.Test;
import unit.feed.controller.AbstractControllerTestBase;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by igor on 06.02.2015.
 */
public class PageKeyItemCreationTest {

    private static final FeedItem[] FEED_ITEMS = {
            AbstractControllerTestBase.create(1),
            AbstractControllerTestBase.create(2),
            AbstractControllerTestBase.create(3),
            AbstractControllerTestBase.create(4),
            AbstractControllerTestBase.create(5)
    };

    private List<FeedItem> items;

    @Before
    public void setUp() {
        this.items = Arrays.asList(FEED_ITEMS);
    }

    @Test
    public void whenKeyItemNotFoundInListThenEmptyPageReturns() {
        final Page<FeedItem> page = Page.create(this.items, "48", 3, true);

        assertTrue(page.first);
        assertTrue(page.last);
        assertTrue(page.items.isEmpty());
    }

}
