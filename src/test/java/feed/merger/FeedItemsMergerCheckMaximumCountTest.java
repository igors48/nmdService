package feed.merger;

import nmd.rss.collector.feed.FeedItem;
import nmd.rss.collector.feed.FeedItemsMergeReport;
import nmd.rss.collector.feed.FeedItemsMerger;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 29.04.13
 */
public class FeedItemsMergerCheckMaximumCountTest {

    public static final FeedItem OLD_FIRST = new FeedItem("oldFirstTitle", "oldFirstDescription", "oldFirstLink", new Date(48));
    public static final FeedItem OLD_SECOND = new FeedItem("oldSecondTitle", "oldSecondDescription", "oldSecondLink", new Date(50));
    public static final FeedItem OLD_THIRD = new FeedItem("oldThirdTitle", "oldThirdDescription", "oldThirdLink", new Date(50));

    public static final FeedItem YOUNG_FIRST = new FeedItem("youngFirstTitle", "youngFirstDescription", "youngFirstLink", new Date(58));
    public static final FeedItem YOUNG_SECOND = new FeedItem("youngSecondTitle", "youngSecondDescription", "youngSecondLink", new Date(60));
    public static final FeedItem YOUNG_THIRD = new FeedItem("youngThirdTitle", "youngThirdDescription", "youngThirdLink", new Date(62));

    private List<FeedItem> olds;
    private List<FeedItem> youngs;

    @Before
    public void before() {
        this.olds = new ArrayList<>();
        this.olds.add(OLD_FIRST);
        this.olds.add(OLD_SECOND);
        this.olds.add(OLD_THIRD);

        this.youngs = new ArrayList<>();
        this.youngs.add(YOUNG_FIRST);
        this.youngs.add(YOUNG_SECOND);
        this.youngs.add(YOUNG_THIRD);
    }

    @Test
    public void afterMergedFeedSizeLesserOrEqualThanMaximum() throws Exception {
        FeedItemsMergeReport report = FeedItemsMerger.merge(this.olds, this.youngs, 5);

        assertEquals(5, report.retained.size() + report.added.size());

        assertEquals(OLD_SECOND, report.retained.get(0));
        assertEquals(OLD_THIRD, report.retained.get(1));

        assertEquals(YOUNG_FIRST, report.added.get(0));
        assertEquals(YOUNG_SECOND, report.added.get(1));
        assertEquals(YOUNG_THIRD, report.added.get(2));
    }

    @Test
    public void allOldsRemovedIfItIsNeed() throws Exception {
        FeedItemsMergeReport report = FeedItemsMerger.merge(this.olds, this.youngs, 3);

        assertEquals(3, report.retained.size() + report.added.size());

        assertEquals(OLD_FIRST, report.removed.get(0));
        assertEquals(OLD_SECOND, report.removed.get(1));
        assertEquals(OLD_THIRD, report.removed.get(2));

        assertTrue(report.retained.isEmpty());

        assertEquals(YOUNG_FIRST, report.added.get(0));
        assertEquals(YOUNG_SECOND, report.added.get(1));
        assertEquals(YOUNG_THIRD, report.added.get(2));
    }

    @Test
    public void someYoungsRemovedIfItIsNeed() throws Exception {
        FeedItemsMergeReport report = FeedItemsMerger.merge(this.olds, this.youngs, 2);

        assertEquals(2, report.retained.size() + report.added.size());

        assertEquals(OLD_FIRST, report.removed.get(0));
        assertEquals(OLD_SECOND, report.removed.get(1));
        assertEquals(OLD_THIRD, report.removed.get(2));

        assertTrue(report.retained.isEmpty());

        assertEquals(YOUNG_SECOND, report.added.get(0));
        assertEquals(YOUNG_THIRD, report.added.get(1));
    }

}
