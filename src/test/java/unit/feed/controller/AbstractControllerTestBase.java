package unit.feed.controller;

import nmd.orb.collector.merger.FeedItemsMergeReport;
import nmd.orb.collector.scheduler.CycleFeedUpdateTaskScheduler;
import nmd.orb.error.ServiceException;
import nmd.orb.feed.FeedHeader;
import nmd.orb.feed.FeedItem;
import nmd.orb.reader.Category;
import nmd.orb.services.*;
import nmd.orb.services.report.CategoryReport;
import nmd.orb.services.report.FeedReadReport;
import org.junit.After;
import org.junit.Before;
import org.mockito.Mockito;
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

    private static final FeedHeader FEED_HEADER = new FeedHeader(UUID.randomUUID(), VALID_FIRST_RSS_FEED_LINK, FEED_TITLE, FEED_DESCRIPTION, FEED_LINK);

    protected static final String FIRST_VALID_RSS_FEED = "<?xml version=\"1.0\"?>\n" +
            "<rss version=\"2.0\" xmlns:atom=\"http://www.w3.org/2005/Atom\">\n" +
            "   <channel>\n" +
            "\t  <title>atitle</title>\n" +
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
            "\t\t<pubDate>Sat, 27 Apr 2013 18:26:00 +0400</pubDate>\n" +
            "\t</item>\n" +
            "    </channel>\n" +
            "</rss>    ";

    protected static final String SECOND_VALID_RSS_FEED = "<?xml version=\"1.0\"?>\n" +
            "<rss version=\"2.0\" xmlns:atom=\"http://www.w3.org/2005/Atom\">\n" +
            "   <channel>\n" +
            "\t  <title>btitle</title>\n" +
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
    protected ImportJobContextRepositoryStub importJobContextRepositoryStub;
    protected ChangeRepositoryStub changeRepositoryStub;

    protected FeedsService feedsService;
    protected UpdatesService updatesService;
    protected ReadsService readsService;
    protected CategoriesService categoriesService;
    protected ResetService resetService;
    protected ImportService importService;
    protected CronService cronService;
    protected ChangeRegistrationService changeRegistrationService;
    protected AutoExportService autoExportService;

    protected ChangeRegistrationService changeRegistrationServiceSpy;
    protected UpdatesService updatesServiceSpy;
    protected ImportService importServiceSpy;
    protected AutoExportService autoExportServiceSpy;

    protected MailService mailServiceMock;

    private TransactionsStub transactionsStub;

    @Before
    public void before() throws ServiceException {
        this.transactionsStub = new TransactionsStub();

        this.fetcherStub = new UrlFetcherStub();
        this.feedHeadersRepositoryStub = new FeedHeadersRepositoryStub();
        this.feedItemsRepositoryStub = new FeedItemsRepositoryStub();
        this.feedUpdateTaskRepositoryStub = new FeedUpdateTaskRepositoryStub();
        this.readFeedItemsRepositoryStub = new ReadFeedItemsRepositoryStub();
        this.feedUpdateTaskSchedulerContextRepositoryStub = new FeedUpdateTaskSchedulerContextRepositoryStub();
        this.categoriesRepositoryStub = new CategoriesRepositoryStub();
        this.importJobContextRepositoryStub = new ImportJobContextRepositoryStub();
        this.changeRepositoryStub = new ChangeRepositoryStub();

        this.feedUpdateTaskSchedulerStub = new CycleFeedUpdateTaskScheduler(this.feedUpdateTaskSchedulerContextRepositoryStub, this.feedUpdateTaskRepositoryStub, this.transactionsStub);

        this.changeRegistrationService = new ChangeRegistrationService(this.changeRepositoryStub);
        this.changeRegistrationServiceSpy = Mockito.spy(this.changeRegistrationService);

        this.mailServiceMock = Mockito.mock(MailService.class);

        this.feedsService = new FeedsService(this.feedHeadersRepositoryStub, this.feedItemsRepositoryStub, this.feedUpdateTaskRepositoryStub, this.readFeedItemsRepositoryStub, this.categoriesRepositoryStub, this.changeRegistrationServiceSpy, this.fetcherStub, this.transactionsStub);
        this.updatesService = new UpdatesService(this.feedHeadersRepositoryStub, this.feedItemsRepositoryStub, this.feedUpdateTaskRepositoryStub, this.feedUpdateTaskSchedulerStub, this.fetcherStub, this.transactionsStub);
        this.updatesServiceSpy = Mockito.spy(this.updatesService);
        this.readsService = new ReadsService(this.feedHeadersRepositoryStub, this.feedItemsRepositoryStub, this.readFeedItemsRepositoryStub, this.fetcherStub, this.transactionsStub);
        this.categoriesService = new CategoriesService(this.categoriesRepositoryStub, this.readFeedItemsRepositoryStub, this.feedHeadersRepositoryStub, this.feedItemsRepositoryStub, this.changeRegistrationServiceSpy, this.transactionsStub);
        this.importService = new ImportService(this.importJobContextRepositoryStub, this.categoriesService, this.feedsService, this.transactionsStub);
        this.importServiceSpy = Mockito.spy(this.importService);
        this.resetService = new ResetService(this.feedHeadersRepositoryStub, this.feedItemsRepositoryStub, this.feedUpdateTaskSchedulerContextRepositoryStub, this.feedUpdateTaskRepositoryStub, this.readFeedItemsRepositoryStub, this.categoriesRepositoryStub, this.importJobContextRepositoryStub, this.changeRepositoryStub, this.changeRegistrationService, this.transactionsStub);
        this.autoExportService = new AutoExportService(this.changeRepositoryStub, this.categoriesService, this.mailServiceMock, this.transactionsStub);
        this.autoExportServiceSpy = Mockito.spy(this.autoExportService);
        this.cronService = new CronService(this.updatesServiceSpy, this.importServiceSpy, this.autoExportServiceSpy);
    }

    @After
    public void tearDown() {
        this.transactionsStub.assertOpenedTransactionNotActive();
    }

    protected UUID addValidFirstRssFeed(final String categoryId) throws ServiceException {
        this.fetcherStub.setData(FIRST_VALID_RSS_FEED);

        return this.feedsService.addFeed(VALID_FIRST_RSS_FEED_LINK, categoryId);
    }

    protected void addValidFirstRssFeed(final String feedTitle, final String categoryId) throws ServiceException {
        this.fetcherStub.setData(FIRST_VALID_RSS_FEED);

        this.feedsService.addFeed(VALID_FIRST_RSS_FEED_LINK, feedTitle, categoryId);
    }

    protected UUID addValidFirstRssFeedToMainCategory() throws ServiceException {
        return addValidFirstRssFeed(Category.MAIN_CATEGORY_ID);
    }

    protected UUID addValidSecondRssFeed(final String categoryId) throws ServiceException {
        this.fetcherStub.setData(SECOND_VALID_RSS_FEED);

        return this.feedsService.addFeed(VALID_SECOND_RSS_FEED_LINK, categoryId);
    }

    protected UUID addValidSecondRssFeedToMainCategory() throws ServiceException {
        return addValidSecondRssFeed(Category.MAIN_CATEGORY_ID);
    }

    protected FeedHeader createSampleFeed(final FeedItem... items) {
        this.feedHeadersRepositoryStub.storeHeader(FEED_HEADER);

        final List<FeedItem> feedItems = Arrays.asList(items);
        final FeedItemsMergeReport feedItemsMergeReport = new FeedItemsMergeReport(new ArrayList<FeedItem>(), feedItems, new ArrayList<FeedItem>());

        this.feedItemsRepositoryStub.storeItems(FEED_HEADER.id, feedItemsMergeReport.getAddedAndRetained());

        return FEED_HEADER;
    }

    public static FeedItem create(final int index) {
        final String indexAsString = String.valueOf(index);

        final String title = "title" + indexAsString;
        final String description = "description" + indexAsString;
        final String link = "http://domain.com/link" + indexAsString;
        final String gotoLink = "http://domain.com/gotoLink" + indexAsString;
        final Date date = new Date(index);
        final boolean dateReal = true;

        return new FeedItem(title, description, link, gotoLink, date, dateReal, indexAsString);
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

    protected static FeedReadReport findForFeed(final UUID feedId, final List<FeedReadReport> reports) {

        for (final FeedReadReport report : reports) {

            if (report.feedId.equals(feedId)) {
                return report;
            }
        }

        return null;
    }

}
