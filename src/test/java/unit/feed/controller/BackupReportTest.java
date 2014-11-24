package unit.feed.controller;

import nmd.orb.feed.FeedHeader;
import nmd.orb.reader.Category;
import nmd.orb.reader.ReadFeedItems;
import nmd.orb.services.report.BackupReport;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static nmd.orb.services.CategoriesService.createBackupReport;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by igor on 23.11.2014.
 */
public class BackupReportTest {

    private static final String CATEGORY_01_ID = UUID.randomUUID().toString();
    private static final String CATEGORY_02_ID = UUID.randomUUID().toString();
    private static final String CATEGORY_03_ID = UUID.randomUUID().toString();

    private static final UUID FEED_HEADER_01_ID = UUID.randomUUID();
    private static final UUID FEED_HEADER_02_ID = UUID.randomUUID();
    private static final UUID FEED_HEADER_03_ID = UUID.randomUUID();

    private static final String HTTP_DOMAIN_COM = "http://domain.com";

    private static final Category CATEGORY_01 = new Category(CATEGORY_01_ID, "01");
    private static final Category CATEGORY_02 = new Category(CATEGORY_02_ID, "02");
    private static final Category CATEGORY_03 = new Category(CATEGORY_03_ID, "03");

    private static final ReadFeedItems READ_FEED_ITEMS_01 = new ReadFeedItems(FEED_HEADER_01_ID, new Date(), new HashSet<String>(), new HashSet<String>(), CATEGORY_01_ID);
    private static final ReadFeedItems READ_FEED_ITEMS_02 = new ReadFeedItems(FEED_HEADER_02_ID, new Date(), new HashSet<String>(), new HashSet<String>(), CATEGORY_02_ID);
    private static final ReadFeedItems READ_FEED_ITEMS_03 = new ReadFeedItems(FEED_HEADER_03_ID, new Date(), new HashSet<String>(), new HashSet<String>(), CATEGORY_02_ID);

    private static final FeedHeader FEED_HEADER_01 = new FeedHeader(FEED_HEADER_01_ID, HTTP_DOMAIN_COM, HTTP_DOMAIN_COM, HTTP_DOMAIN_COM, HTTP_DOMAIN_COM);
    private static final FeedHeader FEED_HEADER_02 = new FeedHeader(FEED_HEADER_02_ID, HTTP_DOMAIN_COM, HTTP_DOMAIN_COM, HTTP_DOMAIN_COM, HTTP_DOMAIN_COM);
    private static final FeedHeader FEED_HEADER_03 = new FeedHeader(FEED_HEADER_03_ID, HTTP_DOMAIN_COM, HTTP_DOMAIN_COM, HTTP_DOMAIN_COM, HTTP_DOMAIN_COM);

    private Set<Category> categories;
    private List<FeedHeader> headers;
    private List<ReadFeedItems> readFeedItems;

    @Before
    public void setUp() {
        this.categories = new HashSet<>();
        this.categories.add(CATEGORY_01);
        this.categories.add(CATEGORY_02);
        this.categories.add(CATEGORY_03);

        this.readFeedItems = new ArrayList<>();
        this.readFeedItems.add(READ_FEED_ITEMS_01);
        this.readFeedItems.add(READ_FEED_ITEMS_02);
        this.readFeedItems.add(READ_FEED_ITEMS_03);

        this.headers = new ArrayList<>();
        this.headers.add(FEED_HEADER_01);
        this.headers.add(FEED_HEADER_02);
        this.headers.add(FEED_HEADER_03);
    }

    @Test
    public void smoke() {
        final BackupReport report = createBackupReport(this.categories, this.headers, this.readFeedItems);

        assertTrue(report.lostLinks.isEmpty());
        assertTrue(report.lostHeaders.isEmpty());

        final Map<Category, Set<FeedHeader>> map = report.map;
        assertEquals(3, map.size());

        final Set<Category> categorySet = map.keySet();
        assertTrue(categorySet.contains(CATEGORY_01));
        assertTrue(categorySet.contains(CATEGORY_02));
        assertTrue(categorySet.contains(CATEGORY_03));

        final Set<FeedHeader> firstCategoryHeaders = map.get(CATEGORY_01);
        assertEquals(1, firstCategoryHeaders.size());
        assertTrue(firstCategoryHeaders.contains(FEED_HEADER_01));

        final Set<FeedHeader> secondCategoryHeaders = map.get(CATEGORY_02);
        assertEquals(2, secondCategoryHeaders.size());
        assertTrue(secondCategoryHeaders.contains(FEED_HEADER_02));
        assertTrue(secondCategoryHeaders.contains(FEED_HEADER_03));

        final Set<FeedHeader> thirdCategoryHeaders = map.get(CATEGORY_03);
        assertTrue(thirdCategoryHeaders.isEmpty());
    }
}
