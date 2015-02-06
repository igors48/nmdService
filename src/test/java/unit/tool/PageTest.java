package unit.tool;

import nmd.orb.util.Page;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 12.08.2014
 */
public class PageTest {

    private List<Integer> list;

    @Before
    public void setUp() throws Exception {
        this.list = asList(0, 1, 2, 3, 4, 5);
    }

    @Test
    public void whenOffsetIsZeroThenFirstTrue() {
        final Page<Integer> page = Page.create(list, 0, 2);

        assertTrue(page.first);
    }

    @Test
    public void whenOffsetIsGreaterThanZeroThenFirstFalse() {
        final Page<Integer> page = Page.create(list, 1, 2);

        assertFalse(page.first);
    }

    @Test
    public void whenOffsetPlusSizeLesserThenListCountThenLastFalse() {
        final Page<Integer> page = Page.create(list, 1, 2);

        assertFalse(page.last);
    }

    @Test
    public void whenOffsetPlusSizeEqualToListCountThenLastTrue() {
        final Page<Integer> page = Page.create(list, 4, 2);

        assertTrue(page.last);
    }

    @Test
    public void whenOffsetPlusSizeGreaterThanListCountThenLastTrue() {
        final Page<Integer> page = Page.create(list, 4, 3);

        assertTrue(page.last);
    }

    @Test
    public void whenRequestedListBiggerThanOriginalListThenEntireOriginalListReturns() {
        final Page<Integer> page = Page.create(list, 0, 50);

        assertEquals(this.list.size(), page.items.size());
    }

    @Test
    public void whenRequestedListHasEqualSizeToOriginalListThenEntireOriginalListReturns() {
        final Page<Integer> page = Page.create(list, 0, 6);

        assertEquals(this.list.size(), page.items.size());
    }

    @Test
    public void whenSizeIsZeroThenNoItemsReturns() {
        final Page<Integer> page = Page.create(list, 2, 0);

        assertTrue(page.items.isEmpty());
    }

    @Test
    public void whenRequestedListSmallerThanOriginalListThenPartOfOriginalListReturns() {
        assertEquals(asList(0, 1), Page.create(list, 0, 2).items);
        assertEquals(asList(1, 2, 3, 4), Page.create(list, 1, 4).items);
        assertEquals(asList(4, 5), Page.create(list, 4, 2).items);
    }

}
