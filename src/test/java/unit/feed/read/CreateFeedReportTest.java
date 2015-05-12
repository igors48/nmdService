package unit.feed.read;

import nmd.orb.feed.FeedHeader;
import nmd.orb.feed.FeedItem;
import nmd.orb.reader.Category;
import nmd.orb.reader.ReadFeedItems;
import nmd.orb.services.ReadsService;
import nmd.orb.services.report.FeedReadReport;
import nmd.orb.sources.Source;
import org.junit.Before;
import org.junit.Test;
import unit.feed.controller.AbstractControllerTestBase;

import java.util.*;

import static org.junit.Assert.*;

/**
 * @author : igu
 */
public class CreateFeedReportTest {

    private static final UUID FEED_HEADER_ID = UUID.randomUUID();

    private static final FeedItem ITEM_01 = AbstractControllerTestBase.create(10);
    private static final FeedItem ITEM_02 = AbstractControllerTestBase.create(20);
    private static final FeedItem ITEM_03 = AbstractControllerTestBase.create(30);
    private static final FeedItem ITEM_04 = AbstractControllerTestBase.create(40);
    private static final FeedItem ITEM_05 = AbstractControllerTestBase.create(50);
    private static final String RSS_FEED_LINK = "http://domain.com/feedLink";
    private static final String TWITTER_FEED_LINK = "https://twitter.com/adme_ru";
    private static final String INSTAGRAM_FEED_LINK = "http://instagram.com/skif48";
    private static final String FEED_TITLE = "feedTitle";
    private static final String FEED_DESCRIPTION = "feedDescription";

    private FeedHeader header;
    private List<FeedItem> items;
    private ReadFeedItems readFeedItems;

    @Before
    public void setUp() {
        this.header = new FeedHeader(FEED_HEADER_ID, RSS_FEED_LINK, FEED_TITLE, FEED_DESCRIPTION, RSS_FEED_LINK);

        this.items = new ArrayList<>();
        this.items.add(ITEM_01);
        this.items.add(ITEM_02);
        this.items.add(ITEM_03);
        this.items.add(ITEM_04);
        this.items.add(ITEM_05);

        final Set<String> readItemsIds = new HashSet<>();

        readItemsIds.add("48"); // not exist id
        readItemsIds.add(ITEM_01.guid);
        readItemsIds.add(ITEM_02.guid);

        final Set<String> readLaterItemsIds = new HashSet<>();

        readLaterItemsIds.add(ITEM_03.guid);
        readLaterItemsIds.add("58"); // not exist id

        this.readFeedItems = new ReadFeedItems(FEED_HEADER_ID, ITEM_03.date, readItemsIds, readLaterItemsIds, Category.MAIN_CATEGORY_ID);
    }

    @Test
    public void feedIdAndTitleAreCopiedFromHeader() {
        final FeedReadReport feedReadReport = ReadsService.createFeedReadReport(this.header, this.items, this.readFeedItems, 0);

        assertEquals(this.header.id, feedReadReport.feedId);
        assertEquals(this.header.title, feedReadReport.feedTitle);
    }

    @Test
    public void lastUpdateTimeIsSetCorrectly() {
        final FeedReadReport feedReadReport = ReadsService.createFeedReadReport(this.header, this.items, this.readFeedItems, 0);

        assertEquals(ITEM_03.date, feedReadReport.lastUpdate);
    }

    @Test
    public void feedTypeIsSetCorrectly() {
        final FeedHeader rssFeedHeader = new FeedHeader(FEED_HEADER_ID, RSS_FEED_LINK, FEED_TITLE, FEED_DESCRIPTION, RSS_FEED_LINK);
        final FeedReadReport rssFeedReadReport = ReadsService.createFeedReadReport(rssFeedHeader, this.items, this.readFeedItems, 0);
        assertEquals(Source.RSS, rssFeedReadReport.feedType);

        final FeedHeader twitterFeedHeader = new FeedHeader(FEED_HEADER_ID, TWITTER_FEED_LINK, FEED_TITLE, FEED_DESCRIPTION, TWITTER_FEED_LINK);
        final FeedReadReport twitterFeedReadReport = ReadsService.createFeedReadReport(twitterFeedHeader, this.items, this.readFeedItems, 0);
        assertEquals(Source.TWITTER, twitterFeedReadReport.feedType);

        final FeedHeader instagramFeedHeader = new FeedHeader(FEED_HEADER_ID, INSTAGRAM_FEED_LINK, FEED_TITLE, FEED_DESCRIPTION, INSTAGRAM_FEED_LINK);
        final FeedReadReport instagramFeedReadReport = ReadsService.createFeedReadReport(instagramFeedHeader, this.items, this.readFeedItems, 0);
        assertEquals(Source.INSTAGRAM, instagramFeedReadReport.feedType);
    }

    @Test
    public void readAndNotReadItemsCountedCorrectly() {
        final FeedReadReport feedReadReport = ReadsService.createFeedReadReport(this.header, this.items, this.readFeedItems, 0);

        assertEquals(2, feedReadReport.read);
        assertEquals(3, feedReadReport.notRead);
    }

    @Test
    public void readLaterItemsCountedCorrectly() {
        final FeedReadReport feedReadReport = ReadsService.createFeedReadReport(this.header, this.items, this.readFeedItems, 0);

        assertEquals(1, feedReadReport.readLater);
    }

    @Test
    public void addedFromLastVisitItemsCountedCorrectly() {
        final FeedReadReport feedReadReport = ReadsService.createFeedReadReport(this.header, this.items, this.readFeedItems, 0);

        assertEquals(2, feedReadReport.addedFromLastVisit);
    }

    @Test
    public void topItemIdAndLinkAreSetCorrectly() {
        final FeedReadReport feedReadReport = ReadsService.createFeedReadReport(this.header, this.items, this.readFeedItems, 0);

        assertEquals(ITEM_04.guid, feedReadReport.topItemId);
        assertEquals(ITEM_04.gotoLink, feedReadReport.topItemLink);
    }

    @Test
    public void whenErrorsCountGreaterThenMaximumThenHasErrorsRaised() {
        final FeedReadReport feedReadReport = ReadsService.createFeedReadReport(this.header, this.items, this.readFeedItems, ReadsService.MAX_SEQUENTIAL_UPDATE_ERRORS_COUNT + 5);

        assertTrue(feedReadReport.hasErrors);
    }

    @Test
    public void whenErrorsCountEqualToMaximumThenHasErrorsRaised() {
        final FeedReadReport feedReadReport = ReadsService.createFeedReadReport(this.header, this.items, this.readFeedItems, ReadsService.MAX_SEQUENTIAL_UPDATE_ERRORS_COUNT);

        assertTrue(feedReadReport.hasErrors);
    }

    @Test
    public void whenErrorsCountLesserThanMaximumThenHasErrorsRaised() {
        final FeedReadReport feedReadReport = ReadsService.createFeedReadReport(this.header, this.items, this.readFeedItems, ReadsService.MAX_SEQUENTIAL_UPDATE_ERRORS_COUNT - 1);

        assertFalse(feedReadReport.hasErrors);
    }

}
