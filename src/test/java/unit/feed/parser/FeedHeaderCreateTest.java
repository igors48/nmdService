package unit.feed.parser;

import nmd.orb.error.ErrorCode;
import nmd.orb.error.ServiceException;
import nmd.orb.feed.FeedHeader;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by igor on 31.01.2015.
 */
public class FeedHeaderCreateTest {

    private UUID id;
    private String feedLink;
    private String title;
    private String description;
    private String link;

    @Before
    public void setUp() {
        this.id = UUID.randomUUID();
        this.feedLink = "http://domain.com";
        this.title = "title";
        this.description = "description";
        this.link = "http://domain2.com";
    }

    @Test
    public void happyFlow() throws ServiceException {
        final FeedHeader header = create();

        assertEquals(header.id, this.id);
        assertEquals(header.title, this.title);
        assertEquals(header.description, this.description);
        assertEquals(header.feedLink, this.feedLink);
        assertEquals(header.link, this.link);
    }

    @Test
    public void whenFeedIdIsInvalidThenErrorOccurs() {
        this.id = null;

        assertError(ErrorCode.INVALID_FEED_ID);
    }

    @Test
    public void whenFeedLinkIsInvalidThenErrorOccurs() {
        this.feedLink = "*";

        assertError(ErrorCode.INVALID_FEED_URL);
    }

    @Test
    public void whenFeedTitleIsInvalidThenErrorOccurs() {
        this.title = "";

        assertError(ErrorCode.INVALID_FEED_TITLE);
    }

    @Test
    public void whenFeedDescriptionIsInvalidThenErrorOccurs() {
        this.description = null;

        assertError(ErrorCode.INVALID_FEED_DESCRIPTION);
    }

    @Test
    public void whenLinkIsInvalidThenErrorOccurs() {
        this.link = "*";

        assertError(ErrorCode.INVALID_URL);
    }

    private void assertError(final ErrorCode errorCode) {

        try {
            create();

            fail();
        } catch (ServiceException exception) {
            assertEquals(errorCode, exception.getError().code);
        }
    }

    private FeedHeader create() throws ServiceException {
        return FeedHeader.create(this.id, this.feedLink, this.title, this.description, this.link);
    }

}
