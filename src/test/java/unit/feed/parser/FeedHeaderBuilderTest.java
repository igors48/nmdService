package unit.feed.parser;

import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.sources.rss.FeedParser;
import org.junit.Test;

import java.util.UUID;

import static nmd.rss.collector.feed.FeedHeader.MAX_DESCRIPTION_AND_TITLE_LENGTH;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.02.14
 */
public class FeedHeaderBuilderTest {

    private static final String URL = "http://domain.com/url";
    private static final String LINK = "http://domain.com/link";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final UUID GUID = UUID.randomUUID();
    private static final String LONG_STRING = "012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789";

    @Test
    public void whenFeedUrlIsNullThenNullReturns() {
        final FeedHeader feedHeader = FeedParser.build(null, LINK, TITLE, DESCRIPTION, GUID);

        assertNull(feedHeader);
    }

    @Test
    public void whenFeedUrlIsEmptyThenNullReturns() {
        final FeedHeader feedHeader = FeedParser.build("", LINK, TITLE, DESCRIPTION, GUID);

        assertNull(feedHeader);
    }

    @Test
    public void whenFeedUrlIsContainsSpacesOnlyThenNullReturns() {
        final FeedHeader feedHeader = FeedParser.build(" ", LINK, TITLE, DESCRIPTION, GUID);

        assertNull(feedHeader);
    }

    @Test
    public void whenFeedUrlIsValidThenItAssigns() {
        final FeedHeader feedHeader = FeedParser.build(URL, LINK, TITLE, DESCRIPTION, GUID);

        assertEquals(URL, feedHeader.feedLink);
    }

    @Test
    public void whenFeedLinkIsNullThenFeedUrlUses() {
        final FeedHeader feedHeader = FeedParser.build(URL, null, TITLE, DESCRIPTION, GUID);

        assertEquals(URL, feedHeader.link);
    }

    @Test
    public void whenFeedLinkIsEmptyThenFeedUrlUses() {
        final FeedHeader feedHeader = FeedParser.build(URL, "", TITLE, DESCRIPTION, GUID);

        assertEquals(URL, feedHeader.link);
    }

    @Test
    public void whenFeedLinkContainsSpacesOnlyThenFeedUrlUses() {
        final FeedHeader feedHeader = FeedParser.build(URL, " ", TITLE, DESCRIPTION, GUID);

        assertEquals(URL, feedHeader.link);
    }

    @Test
    public void whenFeedLinkIsValidThenItAssigns() {
        final FeedHeader feedHeader = FeedParser.build(URL, LINK, TITLE, DESCRIPTION, GUID);

        assertEquals(LINK, feedHeader.link);
    }

    @Test
    public void whenFeedTitleIsNullThenFeedUrlUses() {
        final FeedHeader feedHeader = FeedParser.build(URL, LINK, null, DESCRIPTION, GUID);

        assertEquals(URL, feedHeader.title);
    }

    @Test
    public void whenFeedTitleIsEmptyThenFeedUrlUses() {
        final FeedHeader feedHeader = FeedParser.build(URL, LINK, "", DESCRIPTION, GUID);

        assertEquals(URL, feedHeader.title);
    }

    @Test
    public void whenFeedTitleContainsSpacesOnlyThenFeedUrlUses() {
        final FeedHeader feedHeader = FeedParser.build(URL, LINK, " ", DESCRIPTION, GUID);

        assertEquals(URL, feedHeader.title);
    }

    @Test
    public void whenFeedTitleIsValidThenItAssigns() {
        final FeedHeader feedHeader = FeedParser.build(URL, LINK, TITLE, DESCRIPTION, GUID);

        assertEquals(TITLE, feedHeader.title);
    }

    @Test
    public void whenFeedDescriptionIsNullThenFeedUrlUses() {
        final FeedHeader feedHeader = FeedParser.build(URL, LINK, TITLE, null, GUID);

        assertEquals(URL, feedHeader.description);
    }

    @Test
    public void whenFeedDescriptionIsEmptyThenFeedUrlUses() {
        final FeedHeader feedHeader = FeedParser.build(URL, LINK, TITLE, "", GUID);

        assertEquals(URL, feedHeader.description);
    }

    @Test
    public void whenFeedDescriptionContainsSpacesOnlyThenFeedUrlUses() {
        final FeedHeader feedHeader = FeedParser.build(URL, LINK, TITLE, " ", GUID);

        assertEquals(URL, feedHeader.description);
    }

    @Test
    public void whenFeedDescriptionIsValidThenItAssigns() {
        final FeedHeader feedHeader = FeedParser.build(URL, LINK, TITLE, DESCRIPTION, GUID);

        assertEquals(DESCRIPTION, feedHeader.description);
    }

    @Test
    public void guidAssigns() {
        final FeedHeader feedHeader = FeedParser.build(URL, LINK, TITLE, DESCRIPTION, GUID);

        assertEquals(GUID, feedHeader.id);
    }

    @Test
    public void longTitleAndDescriptionAreCutted() {
        final FeedHeader feedHeader = FeedParser.build(URL, LINK, LONG_STRING, LONG_STRING, GUID);

        assertEquals(MAX_DESCRIPTION_AND_TITLE_LENGTH, feedHeader.title.length());
        assertEquals(MAX_DESCRIPTION_AND_TITLE_LENGTH, feedHeader.description.length());
    }

}
