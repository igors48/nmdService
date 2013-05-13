package feed.merger;

import nmd.rss.collector.feed.FeedItem;
import nmd.rss.collector.feed.FeedItemsMergeReport;
import nmd.rss.collector.feed.FeedItemsMerger;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 29.04.13
 */
public class FeedItemsMergerMergeTest {

    private static final int REALLY_BIG_FEED = 1000;

    private static final FeedItem OLD_FIRST = new FeedItem(UUID.randomUUID(), "oldFirstTitle", "oldFirstDescription", "oldFirstLink", 48);
    private static final FeedItem OLD_SECOND = new FeedItem(UUID.randomUUID(), "oldSecondTitle", "oldSecondDescription", "oldSecondLink", 50);

    private static final FeedItem YOUNG_FIRST = new FeedItem(UUID.randomUUID(), "youngFirstTitle", "youngFirstDescription", "youngFirstLink", 58);
    private static final FeedItem YOUNG_SECOND = new FeedItem(UUID.randomUUID(), "youngSecondTitle", "youngSecondDescription", "youngSecondLink", 60);
    private static final FeedItem OLD_SECOND_DUPLICATE = new FeedItem(UUID.randomUUID(), "oldSecondTitle", "oldSecondDescription", "oldSecondLink", 52);

    private List<FeedItem> olds;
    private List<FeedItem> youngs;

    @Before
    public void before() {
        this.olds = new ArrayList<>();
        this.olds.add(OLD_FIRST);
        this.olds.add(OLD_SECOND);

        this.youngs = new ArrayList<>();
        this.youngs.add(YOUNG_FIRST);
        this.youngs.add(YOUNG_SECOND);
    }

    @Test
    public void allNewFeedsAdded() {
        FeedItemsMergeReport report = FeedItemsMerger.merge(this.olds, this.youngs, 1000);

        assertEquals(2, report.added.size());
        assertEquals(YOUNG_FIRST, report.added.get(0));
        assertEquals(YOUNG_SECOND, report.added.get(1));
    }

    @Test
    public void anyOfOldFeedsDidNotRemove() {
        FeedItemsMergeReport report = FeedItemsMerger.merge(this.olds, this.youngs, 1000);

        assertEquals(2, report.retained.size());
        assertEquals(OLD_FIRST, report.retained.get(0));
        assertEquals(OLD_SECOND, report.retained.get(1));

        assertTrue(report.removed.isEmpty());
    }

    @Test
    public void newItemWithSameLinkAsOldItemAdded() {
        this.youngs.add(OLD_SECOND_DUPLICATE);

        FeedItemsMergeReport report = FeedItemsMerger.merge(this.olds, this.youngs, 1000);

        assertEquals(3, report.added.size());

        assertEquals(OLD_SECOND_DUPLICATE, report.added.get(0));
        assertEquals(YOUNG_FIRST, report.added.get(1));
        assertEquals(YOUNG_SECOND, report.added.get(2));
    }

    @Test
    public void oldItemWithSameLinkAsNewItemRemoved() {
        this.youngs.add(OLD_SECOND_DUPLICATE);

        FeedItemsMergeReport report = FeedItemsMerger.merge(this.olds, this.youngs, REALLY_BIG_FEED);

        assertEquals(1, report.removed.size());

        assertEquals(OLD_SECOND, report.removed.get(0));
    }

    @Test
    public void ifItemsIdenticalThenMergeNoNeeds() throws Exception {
        this.youngs.clear();
        this.youngs.add(OLD_FIRST);
        this.youngs.add(OLD_SECOND);

        FeedItemsMergeReport report = FeedItemsMerger.merge(this.olds, this.youngs, REALLY_BIG_FEED);

        assertTrue(report.added.isEmpty());
        assertTrue(report.removed.isEmpty());

        assertEquals(2, report.retained.size());
        assertTrue(report.retained.contains(OLD_FIRST));
        assertTrue(report.retained.contains(OLD_SECOND));
    }

}
