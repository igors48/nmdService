package unit.feed.controller.importer;

import nmd.orb.error.ErrorCode;
import nmd.orb.error.ServiceException;
import nmd.orb.http.responses.payload.FeedHeaderPayload;
import nmd.orb.services.importer.FeedImportContext;
import nmd.orb.services.importer.FeedImportTaskStatus;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by igor on 06.12.2014.
 */
public class FeedImportContextConversionTest {

    private static final int TRIES_COUNT = 3;

    private FeedHeaderPayload payload;

    @Before
    public void setUp() {
        this.payload = FeedHeaderPayload.create("http://domain.com", UUID.randomUUID().toString(), "title");
        ;
    }

    @Test
    public void smoke() throws ServiceException {
        final FeedImportContext context = FeedImportContext.convert(this.payload, TRIES_COUNT);

        final FeedImportContext expected = new FeedImportContext(payload.feedLink, payload.feedTitle, TRIES_COUNT, FeedImportTaskStatus.WAITING);

        assertEquals(expected, context);
    }

    @Test
    public void whenFeedLinkIsInvalidThenErrorOccurs() {
        payload.feedLink = "*";

        assertConversionError(payload, ErrorCode.INVALID_FEED_URL);
    }

    @Test
    public void whenFeedTitleIsInvalidThenErrorOccurs() {
        payload.feedTitle = "";

        assertConversionError(payload, ErrorCode.INVALID_FEED_TITLE);
    }

    private void assertConversionError(final FeedHeaderPayload payload, final ErrorCode errorCode) {

        try {
            FeedImportContext.convert(payload, 3);

            fail();
        } catch (ServiceException exception) {
            assertEquals(exception.getError().code, errorCode);
        }
    }

}
