package unit.feed.parser;

import nmd.orb.error.ErrorCode;
import nmd.orb.error.ServiceException;
import nmd.orb.feed.FeedHeader;
import nmd.orb.sources.rss.FeedParser;
import org.junit.Test;

import java.util.UUID;

import static nmd.orb.feed.FeedHeader.MAX_DESCRIPTION_AND_TITLE_LENGTH;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.02.14
 */
public class FeedHeaderBuilderTest {

    public static final String LONG_STRING = "012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789";

    private static final String URL = "http://domain.com/url";
    private static final String LINK = "http://domain.com/link";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final UUID GUID = UUID.randomUUID();

    @Test
    public void whenFeedUrlIsNullThenExceptionThrown() {

        try {
            FeedParser.build(null, LINK, TITLE, DESCRIPTION, GUID);

            fail();
        } catch (ServiceException exception) {
            assertEquals(ErrorCode.INVALID_FEED_URL, exception.getError().code);
        }
    }

    @Test
    public void whenFeedUrlIsEmptyThenNullReturns() throws ServiceException {

        try {
            FeedParser.build("", LINK, TITLE, DESCRIPTION, GUID);

            fail();
        } catch (ServiceException exception) {
            assertEquals(ErrorCode.INVALID_FEED_URL, exception.getError().code);
        }
    }

    @Test
    public void whenFeedUrlIsContainsSpacesOnlyThenNullReturns() throws ServiceException {

        try {
            FeedParser.build(" ", LINK, TITLE, DESCRIPTION, GUID);

            fail();
        } catch (ServiceException exception) {
            assertEquals(ErrorCode.INVALID_FEED_URL, exception.getError().code);
        }
    }

    @Test
    public void whenFeedUrlIsValidThenItAssigns() throws ServiceException {
        final FeedHeader feedHeader = FeedParser.build(URL, LINK, TITLE, DESCRIPTION, GUID);

        assertEquals(URL, feedHeader.feedLink);
    }

    @Test
    public void whenFeedLinkIsNullThenFeedUrlUses() throws ServiceException {
        final FeedHeader feedHeader = FeedParser.build(URL, null, TITLE, DESCRIPTION, GUID);

        assertEquals(URL, feedHeader.link);
    }

    @Test
    public void whenFeedLinkIsEmptyThenFeedUrlUses() throws ServiceException {
        final FeedHeader feedHeader = FeedParser.build(URL, "", TITLE, DESCRIPTION, GUID);

        assertEquals(URL, feedHeader.link);
    }

    @Test
    public void whenFeedLinkContainsSpacesOnlyThenFeedUrlUses() throws ServiceException {
        final FeedHeader feedHeader = FeedParser.build(URL, " ", TITLE, DESCRIPTION, GUID);

        assertEquals(URL, feedHeader.link);
    }

    @Test
    public void whenFeedLinkIsValidThenItAssigns() throws ServiceException {
        final FeedHeader feedHeader = FeedParser.build(URL, LINK, TITLE, DESCRIPTION, GUID);

        assertEquals(LINK, feedHeader.link);
    }

    @Test
    public void whenFeedTitleIsNullThenFeedUrlUses() throws ServiceException {
        final FeedHeader feedHeader = FeedParser.build(URL, LINK, null, DESCRIPTION, GUID);

        assertEquals(URL, feedHeader.title);
    }

    @Test
    public void whenFeedTitleIsEmptyThenFeedUrlUses() throws ServiceException {
        final FeedHeader feedHeader = FeedParser.build(URL, LINK, "", DESCRIPTION, GUID);

        assertEquals(URL, feedHeader.title);
    }

    @Test
    public void whenFeedTitleContainsSpacesOnlyThenFeedUrlUses() throws ServiceException {
        final FeedHeader feedHeader = FeedParser.build(URL, LINK, " ", DESCRIPTION, GUID);

        assertEquals(URL, feedHeader.title);
    }

    @Test
    public void whenFeedTitleIsValidThenItAssigns() throws ServiceException {
        final FeedHeader feedHeader = FeedParser.build(URL, LINK, TITLE, DESCRIPTION, GUID);

        assertEquals(TITLE, feedHeader.title);
    }

    @Test
    public void whenFeedDescriptionIsNullThenFeedUrlUses() throws ServiceException {
        final FeedHeader feedHeader = FeedParser.build(URL, LINK, TITLE, null, GUID);

        assertEquals(URL, feedHeader.description);
    }

    @Test
    public void whenFeedDescriptionIsEmptyThenFeedUrlUses() throws ServiceException {
        final FeedHeader feedHeader = FeedParser.build(URL, LINK, TITLE, "", GUID);

        assertEquals(URL, feedHeader.description);
    }

    @Test
    public void whenFeedDescriptionContainsSpacesOnlyThenFeedUrlUses() throws ServiceException {
        final FeedHeader feedHeader = FeedParser.build(URL, LINK, TITLE, " ", GUID);

        assertEquals(URL, feedHeader.description);
    }

    @Test
    public void whenFeedDescriptionIsValidThenItAssigns() throws ServiceException {
        final FeedHeader feedHeader = FeedParser.build(URL, LINK, TITLE, DESCRIPTION, GUID);

        assertEquals(DESCRIPTION, feedHeader.description);
    }

    @Test
    public void guidAssigns() throws ServiceException {
        final FeedHeader feedHeader = FeedParser.build(URL, LINK, TITLE, DESCRIPTION, GUID);

        assertEquals(GUID, feedHeader.id);
    }

    @Test
    public void longTitleAndDescriptionAreCutted() throws ServiceException {
        final FeedHeader feedHeader = FeedParser.build(URL, LINK, LONG_STRING, LONG_STRING, GUID);

        assertEquals(MAX_DESCRIPTION_AND_TITLE_LENGTH, feedHeader.title.length());
        assertEquals(MAX_DESCRIPTION_AND_TITLE_LENGTH, feedHeader.description.length());
    }

}
