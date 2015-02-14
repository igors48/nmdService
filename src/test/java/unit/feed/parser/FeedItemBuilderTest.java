package unit.feed.parser;

import nmd.orb.error.ServiceException;
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
    public void whenLinkIsNullThenNullReturns() throws ServiceException {
        final FeedItem feedItem = build(null, TITLE, DESCRIPTION, ALTERNATE_DESCRIPTION, DATE, CURRENT_DATE, GUID);

        assertNull(feedItem);
    }

    @Test
    public void whenLinkIsEmptyThenNullReturns() throws ServiceException {
        final FeedItem feedItem = build("", TITLE, DESCRIPTION, ALTERNATE_DESCRIPTION, DATE, CURRENT_DATE, GUID);

        assertNull(feedItem);
    }

    @Test
    public void whenLinkContainsSpacesOnlyThenNullReturns() throws ServiceException {
        final FeedItem feedItem = build(" ", TITLE, DESCRIPTION, ALTERNATE_DESCRIPTION, DATE, CURRENT_DATE, GUID);

        assertNull(feedItem);
    }

    @Test
    public void whenLinkIsValidThenItIsAssigned() throws ServiceException {
        final FeedItem feedItem = build(LINK, TITLE, DESCRIPTION, ALTERNATE_DESCRIPTION, DATE, CURRENT_DATE, GUID);

        assertEquals(LINK, feedItem.link);
    }

    @Test
    public void whenDescriptionIsNullThenAlternateDescriptionUses() throws ServiceException {
        final FeedItem feedItem = build(LINK, TITLE, null, ALTERNATE_DESCRIPTION, DATE, CURRENT_DATE, GUID);

        assertEquals(ALTERNATE_DESCRIPTION, feedItem.description);
    }

    @Test
    public void whenDescriptionIsEmptyThenAlternateDescriptionUses() throws ServiceException {
        final FeedItem feedItem = build(LINK, TITLE, "", ALTERNATE_DESCRIPTION, DATE, CURRENT_DATE, GUID);

        assertEquals(ALTERNATE_DESCRIPTION, feedItem.description);
    }

    @Test
    public void whenDescriptionContainsSpacesOnlyThenAlternateDescriptionUses() throws ServiceException {
        final FeedItem feedItem = build(LINK, TITLE, " ", ALTERNATE_DESCRIPTION, DATE, CURRENT_DATE, GUID);

        assertEquals(ALTERNATE_DESCRIPTION, feedItem.description);
    }

    @Test
    public void whenDescriptionIsValidThenItIsAssigned() throws ServiceException {
        final FeedItem feedItem = build(LINK, TITLE, DESCRIPTION, ALTERNATE_DESCRIPTION, DATE, CURRENT_DATE, GUID);

        assertEquals(DESCRIPTION, feedItem.description);
    }

    @Test
    public void whenDateIsNullThenSubstituteDateUsesAndFlagResets() throws ServiceException {
        final FeedItem feedItem = build(LINK, TITLE, DESCRIPTION, ALTERNATE_DESCRIPTION, null, CURRENT_DATE, GUID);

        assertEquals(CURRENT_DATE, feedItem.date);
        assertFalse(feedItem.dateReal);
    }

    @Test
    public void whenDateIsNotNullThenItUsesAndFlagSets() throws ServiceException {
        final FeedItem feedItem = build(LINK, TITLE, DESCRIPTION, ALTERNATE_DESCRIPTION, DATE, CURRENT_DATE, GUID);

        assertEquals(DATE, feedItem.date);
        assertTrue(feedItem.dateReal);
    }

    @Test
    public void whenDateFromFarPastThenCurrentIsUsedInstead() throws ServiceException {
        final Date farPast = new Date(CURRENT_DATE.getTime() - FeedItem.FIFTY_YEARS - FeedItem.TWENTY_FOUR_HOURS);

        final FeedItem feedItem = build(LINK, TITLE, DESCRIPTION, ALTERNATE_DESCRIPTION, farPast, CURRENT_DATE, GUID);

        assertEquals(CURRENT_DATE, feedItem.date);
        assertFalse(feedItem.dateReal);
    }

    @Test
    public void whenDateFromFarFutureThenCurrentIsUsedInstead() throws ServiceException {
        final Date farFuture = new Date(CURRENT_DATE.getTime() + FeedItem.FIFTY_YEARS);

        final FeedItem feedItem = build(LINK, TITLE, DESCRIPTION, ALTERNATE_DESCRIPTION, farFuture, CURRENT_DATE, GUID);

        assertEquals(CURRENT_DATE, feedItem.date);
        assertFalse(feedItem.dateReal);
    }

    @Test
    public void whenTitleIsNullThenLinkUses() throws ServiceException {
        final FeedItem feedItem = build(LINK, null, DESCRIPTION, ALTERNATE_DESCRIPTION, DATE, CURRENT_DATE, GUID);

        assertEquals(LINK, feedItem.title);
    }

    @Test
    public void whenTitleIsEmptyThenLinkUses() throws ServiceException {
        final FeedItem feedItem = build(LINK, "", DESCRIPTION, ALTERNATE_DESCRIPTION, DATE, CURRENT_DATE, GUID);

        assertEquals(LINK, feedItem.title);
    }

    @Test
    public void whenTitleContainsSpacesOnlyThenLinkUses() throws ServiceException {
        final FeedItem feedItem = build(LINK, " ", DESCRIPTION, ALTERNATE_DESCRIPTION, DATE, CURRENT_DATE, GUID);

        assertEquals(LINK, feedItem.title);
    }

    @Test
    public void whenTitleIsValidThenItAssigns() throws ServiceException {
        final FeedItem feedItem = build(LINK, TITLE, DESCRIPTION, ALTERNATE_DESCRIPTION, DATE, CURRENT_DATE, GUID);

        assertEquals(TITLE, feedItem.title);
    }

    @Test
    public void guidAssigns() throws ServiceException {
        final FeedItem feedItem = build(LINK, TITLE, DESCRIPTION, ALTERNATE_DESCRIPTION, DATE, CURRENT_DATE, GUID);

        assertEquals(GUID, feedItem.guid);
    }

}
