package unit.feed.controller;

import nmd.orb.feed.FeedItem;
import nmd.orb.feed.FeedItemShortcut;
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

    private static final List<FeedItemShortcut> FEED_SHORTCUTS = new ArrayList<FeedItemShortcut>() {{
        add(FeedItem.createShortcut(new FeedItem("first", "first", "http://domain.com/first", "http://domain.com/firstGoto", new Date(1), true, "first")));
        add(FeedItem.createShortcut(new FeedItem("second", "second", "http://domain.com/second", "http://domain.com/secondGoto", new Date(2), true, "second")));
        add(FeedItem.createShortcut(new FeedItem("third", "third", "http://domain.com/third", "http://domain.com/thirdGoto", new Date(3), true, "third")));
    }};

    @Test
    public void whenThereAreNoReadItemsThenLastReturns() {
        final FeedItemShortcut last = findFirstNotReadFeedItem(FEED_SHORTCUTS, new HashSet<String>(), new Date(0));

        assertEquals("third", last.guid);
    }

    @Test
    public void whenAllItemsReadThenNullReturns() {
        final FeedItemShortcut last = findFirstNotReadFeedItem(FEED_SHORTCUTS, new HashSet<String>() {{
            add("third");
            add("second");
            add("first");
        }}, new Date(0));

        assertNull(last);
    }

    @Test
    public void whenThereAreNoItemsThenNullReturns() {
        final FeedItemShortcut last = findFirstNotReadFeedItem(new ArrayList<FeedItemShortcut>(), new HashSet<String>() {{
            add("third");
            add("second");
            add("first");
        }}, new Date(0));

        assertNull(last);
    }

    @Test
    public void whenNotReadItemYoungerThanLastUpdatedExistsThenFirstOfItReturns() {
        final FeedItemShortcut last = findFirstNotReadFeedItem(FEED_SHORTCUTS, new HashSet<String>() {{
            add("first");
        }}, new Date(1));

        assertEquals("second", last.guid);
    }

    @Test
    public void whenNotReadItemYoungerThanLastUpdatedNotExistsThenLatestOfPreviousNotReadsReturns() {
        final FeedItemShortcut last = findFirstNotReadFeedItem(FEED_SHORTCUTS, new HashSet<String>() {{
            add("second");
            add("third");
        }}, new Date(3));

        assertEquals("first", last.guid);
    }

}
