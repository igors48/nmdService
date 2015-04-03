package unit.feed.read;

import nmd.orb.feed.FeedHeader;
import nmd.orb.feed.FeedItem;
import nmd.orb.reader.ReadFeedItems;
import nmd.orb.services.ReadsService;
import nmd.orb.services.report.FeedReadReport;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

/**
 * @author : igu
 */
public class CreateFeedReportTest {

    private FeedHeader header;
    private List<FeedItem> items;
    private ReadFeedItems readFeedItems;

    @Before
    public void setUp() {
        final UUID feedHeaderId = UUID.randomUUID();
        this.header = new FeedHeader(feedHeaderId, "http://domain.com/feedLink", "feedTitle", "feedDescription", "http://domain.com/link");
        this.items = new ArrayList<>();
        this.readFeedItems = ReadFeedItems.empty(feedHeaderId);
    }

    @Test
    public void feedIdAndTitleAreCopiedFromHeader() {
        final FeedReadReport feedReadReport = ReadsService.createFeedReadReport(this.header, this.items, this.readFeedItems);

        assertEquals(this.header.id, feedReadReport.feedId);
        assertEquals(this.header.title, feedReadReport.feedTitle);
    }

}
