package unit.feed.controller;

import nmd.orb.feed.FeedItem;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import static nmd.orb.services.ReadsService.findFirstNotReadFeedItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date: 04.12.13
 */
public class ControllerFindFirstNotReadFeedItemTest {

    private static final List<FeedItem> FEED_ITEMS = new ArrayList<FeedItem>() {{
        add(new FeedItem("first", "first", "http://domain.com/first", "http://domain.com/firstGoto", new Date(1), true, "first"));
        add(new FeedItem("second", "second", "http://domain.com/second", "http://domain.com/secondGoto", new Date(2), true, "second"));
        add(new FeedItem("third", "third", "http://domain.com/third", "http://domain.com/thirdGoto", new Date(3), true, "third"));
    }};

    @Test
    public void whenThereAreNoReadItemsThenLastReturns() {
        final FeedItem last = findFirstNotReadFeedItem(FEED_ITEMS, new HashSet<String>(), new Date(0));

        assertEquals("third", last.guid);
    }

    @Test
    public void whenAllItemsReadThenNullReturns() {
        final FeedItem last = findFirstNotReadFeedItem(FEED_ITEMS, new HashSet<String>() {{
            add("third");
            add("second");
            add("first");
        }}, new Date(0));

        assertNull(last);
    }

    @Test
    public void whenThereAreNoItemsThenNullReturns() {
        final FeedItem last = findFirstNotReadFeedItem(new ArrayList<FeedItem>(), new HashSet<String>() {{
            add("third");
            add("second");
            add("first");
        }}, new Date(0));

        assertNull(last);
    }

    @Test
    public void whenNotReadItemYoungerThanLastUpdatedExistsThenFirstOfItReturns() {
        final FeedItem last = findFirstNotReadFeedItem(FEED_ITEMS, new HashSet<String>() {{
            add("first");
        }}, new Date(1));

        assertEquals("second", last.guid);
    }

    @Test
    public void whenNotReadItemYoungerThanLastUpdatedNotExistsThenLatestOfPreviousNotReadsReturns() {
        final FeedItem last = findFirstNotReadFeedItem(FEED_ITEMS, new HashSet<String>() {{
            add("second");
            add("third");
        }}, new Date(3));

        assertEquals("first", last.guid);
    }

}
