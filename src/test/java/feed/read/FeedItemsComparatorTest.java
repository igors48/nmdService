package feed.read;

import nmd.rss.reader.FeedItemsComparisonReport;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static nmd.rss.reader.FeedItemsComparator.findNotReadItems;
import static org.junit.Assert.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 08.09.13
 */
public class FeedItemsComparatorTest {

    private static final String FIRST_ID = "first";
    private static final String SECOND_ID = "second";
    private static final String THIRD_ID = "third";
    private static final String FOURTH_ID = "fourth";
    private static final String FIFTH_ID = "fifth";
    private static final String SIXTH_ID = "sixth";

    @Test
    public void whenFeedBelongsToReadAndStoredThenItRemainsInRead() {
        final List<String> readItems = Arrays.asList(FIRST_ID, SECOND_ID);
        final List<String> storedItems = Arrays.asList(FIRST_ID, THIRD_ID);

        final FeedItemsComparisonReport comparison = findNotReadItems(readItems, storedItems);

        assertTrue(comparison.readItems.contains(FIRST_ID));
        assertFalse(comparison.newItems.contains(FIRST_ID));
    }

    @Test
    public void whenFeedBelongsToReadOnlyThenItRemovesFromRead() {
        final List<String> readItems = Arrays.asList(FIRST_ID, SECOND_ID);
        final List<String> storedItems = Arrays.asList(FOURTH_ID, THIRD_ID);

        final FeedItemsComparisonReport comparison = findNotReadItems(readItems, storedItems);

        assertFalse(comparison.readItems.contains(FIRST_ID));
        assertFalse(comparison.newItems.contains(FIRST_ID));
    }

    @Test
    public void whenFeedBelongsToStoredOnlyThenItAddsToNotRead() {
        final List<String> readItems = Arrays.asList(FIRST_ID, SECOND_ID);
        final List<String> storedItems = Arrays.asList(FOURTH_ID, THIRD_ID);

        final FeedItemsComparisonReport comparison = findNotReadItems(readItems, storedItems);

        assertTrue(comparison.newItems.contains(FOURTH_ID));
        assertFalse(comparison.readItems.contains(FOURTH_ID));
        assertTrue(comparison.newItems.contains(THIRD_ID));
        assertFalse(comparison.readItems.contains(THIRD_ID));
    }

    @Test
    public void smoke() {
        final List<String> readItems = Arrays.asList(FIRST_ID, FIFTH_ID);
        final List<String> storedItems = Arrays.asList(FIRST_ID, SECOND_ID, THIRD_ID, FOURTH_ID, FIFTH_ID, SIXTH_ID);

        final FeedItemsComparisonReport comparison = findNotReadItems(readItems, storedItems);

        assertEquals(4, comparison.newItems.size());
        assertTrue(comparison.newItems.contains(SECOND_ID));
        assertTrue(comparison.newItems.contains(THIRD_ID));
        assertTrue(comparison.newItems.contains(FOURTH_ID));
        assertTrue(comparison.newItems.contains(SIXTH_ID));

        assertEquals(2, comparison.readItems.size());
        assertTrue(comparison.readItems.contains(FIRST_ID));
        assertTrue(comparison.readItems.contains(FIFTH_ID));
    }

}
