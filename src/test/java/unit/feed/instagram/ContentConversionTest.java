package unit.feed.instagram;

import nmd.orb.error.ErrorCode;
import nmd.orb.error.ServiceException;
import nmd.orb.feed.FeedItem;
import nmd.orb.sources.instagram.InstagramClientTools;
import nmd.orb.sources.instagram.entities.ContentEnvelope;
import nmd.orb.sources.instagram.entities.Data;
import nmd.orb.sources.instagram.entities.Meta;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author : igu
 */
public class ContentConversionTest {

    private Date current;
    private ContentEnvelope contentEnvelope;

    @Before
    public void setUp() {
        this.current = new Date();

        this.contentEnvelope = new ContentEnvelope();

        final Meta meta = new Meta();
        meta.code = "200";

        final List<Data> dataList = new ArrayList<>();

        final Data data = DataConversionTest.create();
        dataList.add(data);

        this.contentEnvelope.meta = meta;
        this.contentEnvelope.data = dataList;
    }

    @Test
    public void smoke() throws ServiceException {
        final List<FeedItem> feedItems = InstagramClientTools.convert(this.contentEnvelope, this.current, "first");

        assertFalse(feedItems.isEmpty());
    }

    @Test
    public void whenMetaIsNullThenErrorOccured() {
        this.contentEnvelope.meta = null;

        assertConversionError(ErrorCode.INSTAGRAM_NO_META);
    }

    @Test
    public void whenMetaCodeIsInvalidThenErrorOccured() {
        this.contentEnvelope.meta.code = "400";

        assertConversionError(ErrorCode.INSTAGRAM_WRONG_STATUS_CODE);
    }

    @Test
    public void whenDataIsNullThenErrorOccured() {
        this.contentEnvelope.data = null;

        assertConversionError(ErrorCode.INSTAGRAM_NO_DATA);
    }

    private void assertConversionError(final ErrorCode errorCode) {

        try {
            InstagramClientTools.convert(this.contentEnvelope, this.current, "first");

            fail();
        } catch (ServiceException exception) {
            assertEquals(errorCode, exception.getError().code);
        }
    }

}
