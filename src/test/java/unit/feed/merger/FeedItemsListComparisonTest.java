package unit.feed.merger;

import nmd.orb.collector.feed.FeedItem;
import nmd.orb.collector.feed.FeedItemsMerger;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author : igu
 */
public class FeedItemsListComparisonTest {

    private static FeedItem FIRST = new FeedItem("first-title", "first-description", "http://domain.com/firstLink", new Date(1), true, UUID.randomUUID().toString());
    private static FeedItem FIRST_EQ = new FeedItem("first-title", "first-description", "http://domain.com/firstLink", new Date(1), true, UUID.randomUUID().toString());

    private static FeedItem SECOND = new FeedItem("second-title", "second-description", "http://domain.com/secondLink", new Date(1), true, UUID.randomUUID().toString());
    private static FeedItem SECOND_EQ = new FeedItem("second-title", "second-description", "http://domain.com/secondLink", new Date(1), true, UUID.randomUUID().toString());

    @Test
    public void whenListsEmptyThenTheyEquals() {
        final List<FeedItem> first = new ArrayList<>();
        final List<FeedItem> second = new ArrayList<>();

        final boolean equals = FeedItemsMerger.listEqualsIgnoringGuid(first, second);

        assertTrue(equals);
    }

    @Test
    public void whenListsHaveDiffSizesThenTheyNotEquals() {
        final List<FeedItem> first = Arrays.asList(FIRST);
        final List<FeedItem> second = Arrays.asList(FIRST, SECOND);

        final boolean equals = FeedItemsMerger.listEqualsIgnoringGuid(first, second);

        assertFalse(equals);
    }

    @Test
    public void whenBothListsContainSameItemsThenTheyEquals() {
        final List<FeedItem> first = Arrays.asList(FIRST);
        final List<FeedItem> second = Arrays.asList(FIRST);

        final boolean equals = FeedItemsMerger.listEqualsIgnoringGuid(first, second);

        assertTrue(equals);
    }

    @Test
    public void whenOneListContainsEquivalentOfTheItemFromAnotherListThenTheyEquals() {
        final List<FeedItem> first = Arrays.asList(FIRST);
        final List<FeedItem> second = Arrays.asList(FIRST_EQ);

        final boolean equals = FeedItemsMerger.listEqualsIgnoringGuid(first, second);

        assertTrue(equals);
    }

    @Test
    public void whenListsContainDifferentItemsThenTheyNotEquals() {
        final List<FeedItem> first = Arrays.asList(FIRST);
        final List<FeedItem> second = Arrays.asList(SECOND);

        final boolean equals = FeedItemsMerger.listEqualsIgnoringGuid(first, second);

        assertFalse(equals);
    }

    @Test
    public void differentOrderDoesNotMatter() {
        final List<FeedItem> first = Arrays.asList(FIRST, SECOND);
        final List<FeedItem> second = Arrays.asList(SECOND_EQ, FIRST_EQ);

        final boolean equals = FeedItemsMerger.listEqualsIgnoringGuid(first, second);

        assertTrue(equals);
    }

}
