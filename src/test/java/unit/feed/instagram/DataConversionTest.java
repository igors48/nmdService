package unit.feed.instagram;

import nmd.orb.error.ErrorCode;
import nmd.orb.error.ServiceException;
import nmd.orb.sources.instagram.InstagramClientTools;
import nmd.orb.sources.instagram.entities.Caption;
import nmd.orb.sources.instagram.entities.Content;
import nmd.orb.sources.instagram.entities.Data;
import nmd.orb.sources.instagram.entities.Images;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author : igu
 */
public class DataConversionTest extends AbstractInstagramTestBase {

    private Data data;

    @Before
    public void setUp() {
        Caption caption = new Caption();
        caption.text = "caption";

        Images images = new Images();

        Content imageLowResolution = content("http://domain.com/image-low-resolution", 48, 48);
        Content imageThumbnail = content("http://domain.com/image-thumbnail", 48, 48);
        Content imageStandardResolution = content("http://domain.com/image-standard-resolution", 48, 48);

        images.low_resolution = imageLowResolution;
        images.standard_resolution = imageStandardResolution;
        images.thumbnail = imageThumbnail;

        this.data = new Data();

        this.data.link = "http://domain.com";
        this.data.type = "image";
        this.data.created_time = 48L;
        this.data.caption = caption;
        this.data.images = images;
    }

    @Test
    public void whenLinkIsNullThenErrorOccurs() {
        this.data.link = null;

        assertConversionError(ErrorCode.INSTAGRAM_BAD_DATA_LINK);
    }

    @Test
    public void whenLinkIsInvalidThenErrorOccurs() {
        this.data.link = "*";

        assertConversionError(ErrorCode.INSTAGRAM_BAD_DATA_LINK);
    }

    @Test
    public void whenDataTypeBadThenErrorOccurs() {
        this.data.type = null;
        assertConversionError(ErrorCode.INSTAGRAM_BAD_DATA_TYPE);

        this.data.type = "bad";
        assertConversionError(ErrorCode.INSTAGRAM_BAD_DATA_TYPE);
    }

    @Test
    public void whenNoImagesThenErrorOccurs() {
        this.data.images = null;
        assertConversionError(ErrorCode.INSTAGRAM_NO_IMAGES);
    }

    private void assertConversionError(final ErrorCode errorCode) {

        try {
            InstagramClientTools.convert(this.data);

            fail();
        } catch (ServiceException exception) {
            assertEquals(errorCode, exception.getError().code);
        }
    }

    private Content content(final String url, final int width, final int height) {
        final Content content = new Content();

        content.url = url;
        content.width = width;
        content.height = height;

        return content;
    }

}
