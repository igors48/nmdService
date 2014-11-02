package unit.feed.parser;

import nmd.orb.feed.FeedItem;
import org.junit.Test;

import java.util.Date;

import static nmd.orb.sources.rss.FeedParser.build;
import static org.junit.Assert.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.02.14
 */
public class FeedItemBuilderTest {

    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String LINK = "http://domain.com/link";
    private static final String ALTERNATE_DESCRIPTION = "alternate_description";
    private static final String GUID = "guid";
    private static final Date DATE = new Date(1);
    private static final Date CURRENT_DATE = new Date(2);

    @Test
    public void whenLinkIsNullThenNullReturns() {
        final FeedItem feedItem = build(null, TITLE, DESCRIPTION, ALTERNATE_DESCRIPTION, DATE, CURRENT_DATE, GUID);

        assertNull(feedItem);
    }

    @Test
    public void whenLinkIsEmptyThenNullReturns() {
        final FeedItem feedItem = build("", TITLE, DESCRIPTION, ALTERNATE_DESCRIPTION, DATE, CURRENT_DATE, GUID);

        assertNull(feedItem);
    }

    @Test
    public void whenLinkContainsSpacesOnlyThenNullReturns() {
        final FeedItem feedItem = build(" ", TITLE, DESCRIPTION, ALTERNATE_DESCRIPTION, DATE, CURRENT_DATE, GUID);

        assertNull(feedItem);
    }

    @Test
    public void whenLinkIsValidThenItIsAssigned() {
        final FeedItem feedItem = build(LINK, TITLE, DESCRIPTION, ALTERNATE_DESCRIPTION, DATE, CURRENT_DATE, GUID);

        assertEquals(LINK, feedItem.link);
    }

    @Test
    public void whenDescriptionIsNullThenAlternateDescriptionUses() {
        final FeedItem feedItem = build(LINK, TITLE, null, ALTERNATE_DESCRIPTION, DATE, CURRENT_DATE, GUID);

        assertEquals(ALTERNATE_DESCRIPTION, feedItem.description);
    }

    @Test
    public void whenDescriptionIsEmptyThenAlternateDescriptionUses() {
        final FeedItem feedItem = build(LINK, TITLE, "", ALTERNATE_DESCRIPTION, DATE, CURRENT_DATE, GUID);

        assertEquals(ALTERNATE_DESCRIPTION, feedItem.description);
    }

    @Test
    public void whenDescriptionContainsSpacesOnlyThenAlternateDescriptionUses() {
        final FeedItem feedItem = build(LINK, TITLE, " ", ALTERNATE_DESCRIPTION, DATE, CURRENT_DATE, GUID);

        assertEquals(ALTERNATE_DESCRIPTION, feedItem.description);
    }

    @Test
    public void whenDescriptionIsValidThenItIsAssigned() {
        final FeedItem feedItem = build(LINK, TITLE, DESCRIPTION, ALTERNATE_DESCRIPTION, DATE, CURRENT_DATE, GUID);

        assertEquals(DESCRIPTION, feedItem.description);
    }

    @Test
    public void whenDateIsNullThenSubstituteDateUsesAndFlagResets() {
        final FeedItem feedItem = build(LINK, TITLE, DESCRIPTION, ALTERNATE_DESCRIPTION, null, CURRENT_DATE, GUID);

        assertEquals(CURRENT_DATE, feedItem.date);
        assertFalse(feedItem.dateReal);
    }

    @Test
    public void whenDateIsNotNullThenItUsesAndFlagSets() {
        final FeedItem feedItem = build(LINK, TITLE, DESCRIPTION, ALTERNATE_DESCRIPTION, DATE, CURRENT_DATE, GUID);

        assertEquals(DATE, feedItem.date);
        assertTrue(feedItem.dateReal);
    }

    @Test
    public void whenTitleIsNullThenLinkUses() {
        final FeedItem feedItem = build(LINK, null, DESCRIPTION, ALTERNATE_DESCRIPTION, DATE, CURRENT_DATE, GUID);

        assertEquals(LINK, feedItem.title);
    }

    @Test
    public void whenTitleIsEmptyThenLinkUses() {
        final FeedItem feedItem = build(LINK, "", DESCRIPTION, ALTERNATE_DESCRIPTION, DATE, CURRENT_DATE, GUID);

        assertEquals(LINK, feedItem.title);
    }

    @Test
    public void whenTitleContainsSpacesOnlyThenLinkUses() {
        final FeedItem feedItem = build(LINK, " ", DESCRIPTION, ALTERNATE_DESCRIPTION, DATE, CURRENT_DATE, GUID);

        assertEquals(LINK, feedItem.title);
    }

    @Test
    public void whenTitleIsValidThenItAssigns() {
        final FeedItem feedItem = build(LINK, TITLE, DESCRIPTION, ALTERNATE_DESCRIPTION, DATE, CURRENT_DATE, GUID);

        assertEquals(TITLE, feedItem.title);
    }

    @Test
    public void guidAssigns() {
        final FeedItem feedItem = build(LINK, TITLE, DESCRIPTION, ALTERNATE_DESCRIPTION, DATE, CURRENT_DATE, GUID);

        assertEquals(GUID, feedItem.guid);
    }

}
