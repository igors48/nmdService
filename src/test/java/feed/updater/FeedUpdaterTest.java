package feed.updater;

import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.collector.feed.FeedItem;
import nmd.rss.collector.scheduler.FeedUpdateTask;
import nmd.rss.collector.updater.FeedUpdater;
import nmd.rss.collector.updater.FeedUpdaterException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 01.05.13
 */
public class FeedUpdaterTest {

    private static final String RSS_FEED = "<?xml version=\"1.0\"?>\n" +
            "<rss version=\"2.0\" xmlns:atom=\"http://www.w3.org/2005/Atom\">\n" +
            "   <channel>\n" +
            "\t  <title>3DNews - Daily Digital Digest: Новости Hardware</title>\n" +
            "\t  <link>http://www.3dnews.ru/</link>\n" +
            "\t  <description>Новости Hardware на 3DNews</description>\n" +
            "\t<item>\n" +
            "\t\t<title>Смартфон LG Optimus F5 выходит во Франции 29 апреля, после чего появится и на других рынках</title>\n" +
            "\t\t<link>http://www.3dnews.ru/news/644791/</link>\n" +
            "\t\t<description><![CDATA[<img align=\"left\" hspace=\"15\" vspace=\"10\" src=\"http://www.3dnews.ru/images/rss_icons/rss_hardware_blue2.jpg\" border=\"0\" height=\"85\" width=\"120\"/>Аппараты LG серии F рассчитаны на более массовый рынок, чем флагманы вроде HTC One или Samsung Galaxy S4, но это не мешает корейской компании обеспечить их хорошими характеристиками и поддержкой сетей LTE. Смартфон LG Optimus F5 был представлен вместе с...]]></description>\n" +
            "\t\t<pubDate>Sun, 28 Apr 2013 18:26:00 +0400</pubDate>\n" +
            "\t</item>\n" +
            "\t<item>\n" +
            "\t\t<title>Google Glass уже взломали</title>\n" +
            "\t\t<link>http://www.3dnews.ru/news/644778/</link>\n" +
            "\t\t<description><![CDATA[<img align=\"left\" hspace=\"15\" vspace=\"10\" src=\"http://www.3dnews.ru/images/rss_icons/rss_hardware_blue2.jpg\" border=\"0\" height=\"85\" width=\"120\"/>ChromeOS-разработчик и хакер Лиам МакЛафлин (Liam McLoughlin) рассказал в своем Twitter, что не только понял, как получить root-доступ к Google Glass, но также сообщил, что сделать это очень просто.Так как Google Glass работает на операционной системе Android, это дает независимым...]]></description>\n" +
            "\t\t<pubDate>Sun, 28 Apr 2013 18:26:00 +0400</pubDate>\n" +
            "\t</item>\n" +
            "    </channel>\n" +
            "</rss>    ";

    private static final UUID HEADER_ID = UUID.randomUUID();
    private static final String HEADER_TITLE = "headerTitle";
    private static final String HEADER_DESCRIPTION = "headerDescription";
    private static final String HEADER_LINK = "headerLink";
    private static final UUID ITEM_ID = UUID.randomUUID();
    private static final String ITEM_TITLE = "itemTitle";
    private static final String ITEM_DESCRIPTION = "itemDescription";
    private static final String ITEM_LINK = "itemLink";
    private static final int ITEM_TIMESTAMP = 48;
    private static final int MAX_FEED_ITEMS_COUNT = 1000;

    private FeedUpdateTaskSchedulerStub taskSchedulerStub;
    private FeedServiceStub feedServiceStub;
    private UrlFetcherStub urlFetcherStub;

    @Before
    public void before() {
        this.taskSchedulerStub = new FeedUpdateTaskSchedulerStub();

        FeedHeader header = new FeedHeader(HEADER_ID, HEADER_TITLE, HEADER_DESCRIPTION, HEADER_LINK);
        FeedItem item = new FeedItem(ITEM_ID, ITEM_TITLE, ITEM_DESCRIPTION, ITEM_LINK, ITEM_TIMESTAMP);
        List<FeedItem> items = new ArrayList<>();
        items.add(item);
        this.feedServiceStub = new FeedServiceStub(header, items);

        this.urlFetcherStub = new UrlFetcherStub();
        this.urlFetcherStub.setData(RSS_FEED);
    }

    @Test
    public void ifNoScheduledTasksNothingUpdated() throws FeedUpdaterException {
        FeedUpdater.update(this.taskSchedulerStub, this.feedServiceStub, this.urlFetcherStub, MAX_FEED_ITEMS_COUNT);

        assertNoUpdate();
    }

    @Test
    public void ifHeaderNotFoundNothingUpdated() throws FeedUpdaterException {
        FeedUpdateTask task = new FeedUpdateTask(UUID.randomUUID(), UUID.randomUUID());
        this.taskSchedulerStub.setTask(task);

        this.feedServiceStub.setHeader(null);

        FeedUpdater.update(this.taskSchedulerStub, this.feedServiceStub, this.urlFetcherStub, MAX_FEED_ITEMS_COUNT);

        assertNoUpdate();
    }

    @Test
    public void existentFeedHandlesCorrectly() throws FeedUpdaterException {
        FeedUpdateTask task = new FeedUpdateTask(UUID.randomUUID(), UUID.randomUUID());
        this.taskSchedulerStub.setTask(task);

        FeedUpdater.update(this.taskSchedulerStub, this.feedServiceStub, this.urlFetcherStub, MAX_FEED_ITEMS_COUNT);

        List<FeedItem> removed = this.feedServiceStub.getRemoved();
        List<FeedItem> retained = this.feedServiceStub.getRetained();
        List<FeedItem> added = this.feedServiceStub.getAdded();

        assertTrue(removed.isEmpty());
        assertEquals(1, retained.size());
        assertEquals(2, added.size());
    }

    @Test
    public void newFeedHandlesCorrectly() throws FeedUpdaterException {
        FeedUpdateTask task = new FeedUpdateTask(UUID.randomUUID(), UUID.randomUUID());
        this.taskSchedulerStub.setTask(task);

        this.feedServiceStub.setItems(null);

        FeedUpdater.update(this.taskSchedulerStub, this.feedServiceStub, this.urlFetcherStub, MAX_FEED_ITEMS_COUNT);

        List<FeedItem> removed = this.feedServiceStub.getRemoved();
        List<FeedItem> retained = this.feedServiceStub.getRetained();
        List<FeedItem> added = this.feedServiceStub.getAdded();

        assertTrue(removed.isEmpty());
        assertTrue(retained.isEmpty());
        assertEquals(2, added.size());
    }

    private void assertNoUpdate() {
        List<FeedItem> removed = this.feedServiceStub.getRemoved();
        List<FeedItem> retained = this.feedServiceStub.getRetained();
        List<FeedItem> added = this.feedServiceStub.getAdded();

        assertNull(removed);
        assertNull(retained);
        assertNull(added);
    }

}
