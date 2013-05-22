package feed.exporter;

import feed.updater.FeedServiceStub;
import nmd.rss.collector.exporter.ExporterServletTools;
import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.collector.feed.FeedItem;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertTrue;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.05.13
 */
public class ExporterServletToolsExportFeedTest {

    private FeedServiceStub feedService;

    @Before
    public void before() {
        final FeedHeader header = new FeedHeader(UUID.randomUUID(), "title", "description", "link");

        final FeedItem item = new FeedItem("title", "description", "link", new Date());
        final List<FeedItem> items = new ArrayList<>();
        items.add(item);

        this.feedService = new FeedServiceStub(header, items);

    }

    @Test
    public void ifFeedHeaderNotFoundThanEmptyResultReturns() throws Exception {
        this.feedService.setHeader(null);

        final String data = ExporterServletTools.exportFeed(UUID.randomUUID(), this.feedService);

        assertTrue(data.isEmpty());
    }

    @Test
    public void ifFeedItemsNotFoundThanEmptyResultReturns() throws Exception {
        this.feedService.setItems(null);

        final String data = ExporterServletTools.exportFeed(UUID.randomUUID(), this.feedService);

        assertTrue(data.isEmpty());
    }

    @Test
    public void whenHeaderAndItemsAreOkResultIsNotEmpty() throws Exception {
        final String data = ExporterServletTools.exportFeed(UUID.randomUUID(), this.feedService);

        assertTrue(!data.isEmpty());
    }

}
