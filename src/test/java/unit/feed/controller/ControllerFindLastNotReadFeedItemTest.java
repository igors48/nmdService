package unit.feed.controller;

import nmd.rss.collector.feed.FeedItem;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import static nmd.rss.collector.controller.ControlService.findLastNotReadFeedItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * User: igu
 * Date: 04.12.13
 */
public class ControllerFindLastNotReadFeedItemTest {

    private static final List<FeedItem> FEED_ITEMS = new ArrayList<FeedItem>() {{
        add(new FeedItem("first", "first", "first", new Date(0), "first"));
        add(new FeedItem("second", "second", "second", new Date(1), "second"));
        add(new FeedItem("third", "third", "third", new Date(2), "third"));
    }};

    @Test
    public void whenThereAreNoReadItemsThenLastReturns() {
        final FeedItem last = findLastNotReadFeedItem(FEED_ITEMS, new HashSet<String>());

        assertEquals("third", last.guid);
    }

    @Test
    public void whenThereIsReadItemsThenLastNotReadReturns() {
        FeedItem last = findLastNotReadFeedItem(FEED_ITEMS, new HashSet<String>() {{
            add("third");
        }});

        assertEquals("second", last.guid);

        last = findLastNotReadFeedItem(FEED_ITEMS, new HashSet<String>() {{
            add("third");
            add("first");
        }});

        assertEquals("second", last.guid);
    }

    @Test
    public void whenAllItemsReadThenNullReturns() {
        final FeedItem last = findLastNotReadFeedItem(FEED_ITEMS, new HashSet<String>() {{
            add("third");
            add("second");
            add("first");
        }});

        assertNull(last);
    }

    @Test
    public void whenThereAreNoItemsThenNullReturns() {
        final FeedItem last = findLastNotReadFeedItem(new ArrayList<FeedItem>(), new HashSet<String>() {{
            add("third");
            add("second");
            add("first");
        }});

        assertNull(last);
    }

}
