package feed.merger;

import nmd.rss.collector.FeedItem;
import nmd.rss.collector.FeedItemsMergeReport;
import nmd.rss.collector.FeedItemsMerger;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 29.04.13
 */
public class FeedItemsMergerTest {

    //test maximum count of item
    //items sort from old to new

    public static final FeedItem OLD_FIRST = new FeedItem("oldFirstTitle", "oldFirstDescription", "oldFirstLink", 48);
    public static final FeedItem OLD_SECOND = new FeedItem("oldSecondTitle", "oldSecondDescription", "oldSecondLink", 50);

    public static final FeedItem YOUNG_FIRST = new FeedItem("youngFirstTitle", "youngFirstDescription", "youngFirstLink", 58);
    public static final FeedItem YOUNG_SECOND = new FeedItem("youngSecondTitle", "youngSecondDescription", "youngSecondLink", 60);

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
        FeedItemsMergeReport report = FeedItemsMerger.merge(olds, youngs, 1000);

        assertEquals(2, report.added.size());
        assertEquals(YOUNG_FIRST, report.added.get(0));
        assertEquals(YOUNG_SECOND, report.added.get(1));
    }

    @Test
    public void anyOfOldFeedsDidNotRemove() {


    }

    @Test
    public void newItemWithSameLinkAsOldItemAdded() {


    }

    @Test
    public void oldItemWithSameLinkAsNewItemRemoved() {


    }
}
