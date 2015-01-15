package unit.feed.controller.importer;

import nmd.orb.error.ErrorCode;
import nmd.orb.error.ServiceException;
import nmd.orb.http.responses.payload.FeedHeaderPayload;
import nmd.orb.services.importer.FeedImportContext;
import nmd.orb.services.importer.FeedImportTaskStatus;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by igor on 06.12.2014.
 */
public class FeedImportContextConversionTest extends PayloadConversionTestBase {

    @Test
    public void smoke() throws ServiceException {
        final FeedImportContext context = FeedImportContext.convert(this.feedHeaderPayload, TRIES_COUNT);

        final FeedImportContext expected = new FeedImportContext(this.feedHeaderPayload.feedLink, this.feedHeaderPayload.feedTitle, TRIES_COUNT, FeedImportTaskStatus.WAITING);

        assertEquals(expected, context);
    }

    @Test
    public void whenFeedLinkIsInvalidThenErrorOccurs() {
        this.feedHeaderPayload.feedLink = "*";

        assertConversionError(this.feedHeaderPayload, ErrorCode.INVALID_FEED_URL);
    }

    @Test
    public void whenFeedTitleIsInvalidThenErrorOccurs() {
        this.feedHeaderPayload.feedTitle = "";

        assertConversionError(this.feedHeaderPayload, ErrorCode.INVALID_FEED_TITLE);
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
