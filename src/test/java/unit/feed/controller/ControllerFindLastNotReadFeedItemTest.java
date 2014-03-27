package unit.feed.controller;

import nmd.rss.collector.feed.FeedItem;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import static nmd.rss.collector.controller.ReadsService.findLastNotReadFeedItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * User: igu
 * Date: 04.12.13
 */
public class ControllerFindLastNotReadFeedItemTest {

    private static final List<FeedItem> FEED_ITEMS = new ArrayList<FeedItem>() {{
        add(new FeedItem("first", "first", "http://domain.com/first", new Date(0), true, "first"));
        add(new FeedItem("second", "second", "http://domain.com/second", new Date(1), true, "second"));
        add(new FeedItem("third", "third", "http://domain.com/third", new Date(2), true, "third"));
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
