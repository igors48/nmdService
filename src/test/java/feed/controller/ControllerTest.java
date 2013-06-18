package feed.controller;

import feed.scheduler.FeedUpdateTaskRepositoryStub;
import feed.updater.UrlFetcherStub;
import nmd.rss.collector.controller.ControlService;
import nmd.rss.collector.controller.ControllerException;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static nmd.rss.collector.util.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 25.05.13
 */
public class ControllerTest {

    private static final String VALID_RSS_FEED_LINK = "valid-feed-link";
    private static final String VALID_RSS_FEED = "<?xml version=\"1.0\"?>\n" +
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
    private static final String INVALID_RSS_FEED = "<?xml version=\"1.0\"?>\n" +
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

    private UrlFetcherStub fetcherStub;
    private TransactionsStub transactionsStub;
    private FeedHeadersRepositoryStub feedHeadersRepositoryStub;
    private FeedItemsRepositoryStub feedItemsRepositoryStub;
    private FeedUpdateTaskRepositoryStub feedUpdateTaskRepositoryStub;
    private ControlService controlService;

    @Before
    public void before() {
        this.fetcherStub = new UrlFetcherStub();
        this.transactionsStub = new TransactionsStub();
        this.feedHeadersRepositoryStub = new FeedHeadersRepositoryStub();
        this.feedItemsRepositoryStub = new FeedItemsRepositoryStub();
        this.feedUpdateTaskRepositoryStub = new FeedUpdateTaskRepositoryStub();

        this.controlService = new ControlService(this.feedHeadersRepositoryStub, this.feedItemsRepositoryStub, this.feedUpdateTaskRepositoryStub, this.fetcherStub, this.transactionsStub);
    }

    @Test
    public void whenFeedFetchedOkAndParsedOkItAdds() throws ControllerException {
        final UUID id = addValidRssFeed(VALID_RSS_FEED);

        assertNotNull(id);
    }

    @Test
    public void whenFeedWithSameLinkAddedSecondTimeThenPreviousIdReturns() throws ControllerException {
        final UUID firstId = addValidRssFeed(VALID_RSS_FEED);
        final UUID secondId = addValidRssFeed(VALID_RSS_FEED);

        assertEquals(firstId, secondId);
    }

    @Test
    public void whenFeedWithSameLinkButInDifferentCaseAddedSecondTimeThenPreviousIdReturns() throws ControllerException {
        this.fetcherStub.setData(VALID_RSS_FEED);

        final UUID firstId = controlService.addFeed(VALID_RSS_FEED_LINK.toUpperCase());
        final UUID secondId = controlService.addFeed(VALID_RSS_FEED_LINK);

        assertEquals(firstId, secondId);
    }

    @Test(expected = ControllerException.class)
    public void whenFeedCanNotBeParsedThenExceptionOccurs() throws ControllerException {
        this.fetcherStub.setData(INVALID_RSS_FEED);

        controlService.addFeed(VALID_RSS_FEED_LINK);
    }

    private UUID addValidRssFeed(final String feedData) throws ControllerException {
        this.fetcherStub.setData(feedData);

        return controlService.addFeed(VALID_RSS_FEED_LINK);
    }

}
