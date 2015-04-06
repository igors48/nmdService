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

import static org.junit.Assert.assertEquals;

/**
 * @author : igu
 */
public class CreateFeedReportTest {

    private static final UUID FEED_HEADER_ID = UUID.randomUUID();

    private static final FeedItem ITEM_01 = AbstractControllerTestBase.create(1);
    private static final FeedItem ITEM_02 = AbstractControllerTestBase.create(1);
    private static final FeedItem ITEM_03 = AbstractControllerTestBase.create(1);
    private static final FeedItem ITEM_04 = AbstractControllerTestBase.create(1);
    private static final FeedItem ITEM_05 = AbstractControllerTestBase.create(1);
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

        final Set<String> readItemIds = new HashSet<>();

        this.readFeedItems = new ReadFeedItems(FEED_HEADER_ID, ITEM_05.date, readItemIds, new HashSet<String>(), Category.MAIN_CATEGORY_ID);
    }

    /*
    public final UUID feedId;
    public final Source feedType;
    public final String feedTitle;
    public final int read;
    public final int notRead;
    public final int readLater;
    public final int addedFromLastVisit;
    public final String topItemId;
    public final String topItemLink;
    public final Date lastUpdate;

     */
    @Test
    public void feedIdAndTitleAreCopiedFromHeader() {
        final FeedReadReport feedReadReport = ReadsService.createFeedReadReport(this.header, this.items, this.readFeedItems);

        assertEquals(this.header.id, feedReadReport.feedId);
        assertEquals(this.header.title, feedReadReport.feedTitle);
    }

    @Test
    public void feedTypeIsSetCorrectly() {
        final FeedHeader rssFeedHeader = new FeedHeader(FEED_HEADER_ID, RSS_FEED_LINK, FEED_TITLE, FEED_DESCRIPTION, RSS_FEED_LINK);
        final FeedReadReport rssFeedReadReport = ReadsService.createFeedReadReport(rssFeedHeader, this.items, this.readFeedItems);
        assertEquals(Source.RSS, rssFeedReadReport.feedType);

        final FeedHeader twitterFeedHeader = new FeedHeader(FEED_HEADER_ID, TWITTER_FEED_LINK, FEED_TITLE, FEED_DESCRIPTION, TWITTER_FEED_LINK);
        final FeedReadReport twitterFeedReadReport = ReadsService.createFeedReadReport(twitterFeedHeader, this.items, this.readFeedItems);
        assertEquals(Source.TWITTER, twitterFeedReadReport.feedType);

        final FeedHeader instagramFeedHeader = new FeedHeader(FEED_HEADER_ID, INSTAGRAM_FEED_LINK, FEED_TITLE, FEED_DESCRIPTION, INSTAGRAM_FEED_LINK);
        final FeedReadReport instagramFeedReadReport = ReadsService.createFeedReadReport(instagramFeedHeader, this.items, this.readFeedItems);
        assertEquals(Source.INSTAGRAM, instagramFeedReadReport.feedType);
    }

}
