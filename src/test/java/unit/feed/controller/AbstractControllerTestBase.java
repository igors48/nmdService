package unit.feed.controller;

import nmd.rss.collector.controller.*;
import nmd.rss.collector.error.ServiceException;
import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.collector.feed.FeedItem;
import nmd.rss.collector.feed.FeedItemsMergeReport;
import nmd.rss.collector.scheduler.CycleFeedUpdateTaskScheduler;
import nmd.rss.reader.Category;
import org.junit.Before;
import unit.feed.controller.stub.*;
import unit.feed.scheduler.FeedUpdateTaskRepositoryStub;
import unit.feed.scheduler.FeedUpdateTaskSchedulerContextRepositoryStub;

import java.util.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 18.06.13
 */
public abstract class AbstractControllerTestBase {

    protected static final String VALID_FIRST_RSS_FEED_LINK = "http://domain.com/valid-first-feed-link";
    protected static final String VALID_SECOND_RSS_FEED_LINK = "http://domain.com/valid-second-feed-link";
    protected static final String FEED_TITLE = "title";
    protected static final String FEED_DESCRIPTION = "description";
    protected static final String FEED_LINK = "http://domain.com/link";

    protected static final String FIRST_FEED_ITEM_TITLE = "first_title";
    protected static final String FIRST_FEED_ITEM_DESCRIPTION = "first_description";
    protected static final String FIRST_FEED_ITEM_LINK = "http://domain.com/first_link";
    protected static final String FIRST_FEED_ITEM_GUID = "first_guid";

    protected static final String SECOND_FEED_ITEM_TITLE = "second_title";
    protected static final String SECOND_FEED_ITEM_DESCRIPTION = "second_description";
    protected static final String SECOND_FEED_ITEM_LINK = "http://domain.com/second_link";
    protected static final String SECOND_FEED_ITEM_GUID = "second_guid";

    protected static final FeedItem FIRST_FEED_ITEM = new FeedItem(FIRST_FEED_ITEM_TITLE, FIRST_FEED_ITEM_DESCRIPTION, FIRST_FEED_ITEM_LINK, new Date(1), true, FIRST_FEED_ITEM_GUID);
    protected static final FeedItem SECOND_FEED_ITEM = new FeedItem(SECOND_FEED_ITEM_TITLE, SECOND_FEED_ITEM_DESCRIPTION, SECOND_FEED_ITEM_LINK, new Date(2), true, SECOND_FEED_ITEM_GUID);

    private static final FeedHeader FEED_HEADER = new FeedHeader(UUID.randomUUID(), VALID_FIRST_RSS_FEED_LINK, FEED_TITLE, FEED_DESCRIPTION, FEED_LINK);

    protected static final String VALID_RSS_FEED = "<?xml version=\"1.0\"?>\n" +
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

    protected static final String INVALID_RSS_FEED = "<?xml version=\"1.0\"?>\n" +
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

    protected UrlFetcherStub fetcherStub;
    protected CycleFeedUpdateTaskScheduler feedUpdateTaskSchedulerStub;

    protected FeedHeadersRepositoryStub feedHeadersRepositoryStub;
    protected FeedItemsRepositoryStub feedItemsRepositoryStub;
    protected FeedUpdateTaskRepositoryStub feedUpdateTaskRepositoryStub;
    protected ReadFeedItemsRepositoryStub readFeedItemsRepositoryStub;
    protected FeedUpdateTaskSchedulerContextRepositoryStub feedUpdateTaskSchedulerContextRepositoryStub;
    protected CategoriesRepositoryStub categoriesRepositoryStub;

    protected FeedsService feedsService;
    protected UpdatesService updatesService;
    protected ReadsService readsService;
    protected CategoriesService categoriesService;

    @Before
    public void before() throws ServiceException {
        final TransactionsStub transactionsStub = new TransactionsStub();

        this.fetcherStub = new UrlFetcherStub();
        this.feedHeadersRepositoryStub = new FeedHeadersRepositoryStub();
        this.feedItemsRepositoryStub = new FeedItemsRepositoryStub();
        this.feedUpdateTaskRepositoryStub = new FeedUpdateTaskRepositoryStub();
        this.readFeedItemsRepositoryStub = new ReadFeedItemsRepositoryStub();
        this.feedUpdateTaskSchedulerContextRepositoryStub = new FeedUpdateTaskSchedulerContextRepositoryStub();
        this.categoriesRepositoryStub = new CategoriesRepositoryStub();

        this.feedUpdateTaskSchedulerStub = new CycleFeedUpdateTaskScheduler(this.feedUpdateTaskSchedulerContextRepositoryStub, this.feedUpdateTaskRepositoryStub, transactionsStub);

        this.feedsService = new FeedsService(this.feedHeadersRepositoryStub, this.feedItemsRepositoryStub, this.feedUpdateTaskRepositoryStub, this.readFeedItemsRepositoryStub, this.categoriesRepositoryStub, this.feedUpdateTaskSchedulerContextRepositoryStub, this.fetcherStub, transactionsStub);
        this.updatesService = new UpdatesService(this.feedHeadersRepositoryStub, this.feedItemsRepositoryStub, this.feedUpdateTaskRepositoryStub, this.feedUpdateTaskSchedulerStub, this.fetcherStub, transactionsStub);
        this.readsService = new ReadsService(this.feedHeadersRepositoryStub, this.feedItemsRepositoryStub, this.readFeedItemsRepositoryStub, this.fetcherStub, transactionsStub);
        this.categoriesService = new CategoriesService(this.categoriesRepositoryStub, this.readFeedItemsRepositoryStub, this.feedHeadersRepositoryStub, this.feedItemsRepositoryStub, transactionsStub);
    }

    protected UUID addValidFirstRssFeed(final String categoryId) throws ServiceException {
        this.fetcherStub.setData(VALID_RSS_FEED);

        return this.feedsService.addFeed(VALID_FIRST_RSS_FEED_LINK, categoryId);
    }

    protected UUID addValidFirstRssFeedToMainCategory() throws ServiceException {
        return addValidFirstRssFeed(Category.MAIN_CATEGORY_ID);
    }

    protected UUID addValidSecondRssFeed(final String categoryId) throws ServiceException {
        this.fetcherStub.setData(VALID_RSS_FEED);

        return this.feedsService.addFeed(VALID_SECOND_RSS_FEED_LINK, categoryId);
    }

    protected UUID addValidSecondRssFeedToMainCategory() throws ServiceException {
        return addValidSecondRssFeed(Category.MAIN_CATEGORY_ID);
    }

    protected FeedHeader createFeedWithOneItem() {
        return createSampleFeed(FIRST_FEED_ITEM);
    }

    protected FeedHeader createFeedWithTwoItems() {
        return createSampleFeed(FIRST_FEED_ITEM, SECOND_FEED_ITEM);
    }

    protected FeedHeader createSampleFeed(final FeedItem... items) {
        this.feedHeadersRepositoryStub.storeHeader(FEED_HEADER);

        final List<FeedItem> feedItems = Arrays.asList(items);
        final FeedItemsMergeReport feedItemsMergeReport = new FeedItemsMergeReport(new ArrayList<FeedItem>(), feedItems, new ArrayList<FeedItem>());

        this.feedItemsRepositoryStub.storeItems(FEED_HEADER.id, feedItemsMergeReport.getAddedAndRetained());

        return FEED_HEADER;
    }

    protected static void pauseOneMillisecond() {

        try {
            Thread.sleep(1);
        } catch (InterruptedException ignore) {
            // empty
        }
    }

    protected static CategoryReport findForCategory(final String categoryId, final List<CategoryReport> categoryReports) {

        for (final CategoryReport report : categoryReports) {

            if (report.id.equals(categoryId)) {
                return report;
            }
        }

        return null;
    }

}