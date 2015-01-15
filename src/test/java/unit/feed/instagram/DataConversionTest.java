package unit.feed.instagram;

import nmd.orb.error.ErrorCode;
import nmd.orb.error.ServiceException;
import nmd.orb.feed.FeedItem;
import nmd.orb.sources.instagram.InstagramClientTools;
import nmd.orb.sources.instagram.entities.Caption;
import nmd.orb.sources.instagram.entities.Content;
import nmd.orb.sources.instagram.entities.Data;
import nmd.orb.sources.instagram.entities.Images;
import org.junit.Before;
import org.junit.Test;
import unit.feed.parser.FeedHeaderBuilderTest;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * @author : igu
 */
public class DataConversionTest {

    private static final String CAPTION = "caption";
    private static final String HTTP_DOMAIN_COM_IMAGE_LOW_RESOLUTION = "http://domain.com/image-low-resolution";
    private static final String HTTP_DOMAIN_COM_IMAGE_THUMBNAIL = "http://domain.com/image-thumbnail";
    private static final String HTTP_DOMAIN_COM_IMAGE_STANDARD_RESOLUTION = "http://domain.com/image-standard-resolution";
    private static final String HTTP_DOMAIN_COM = "http://domain.com";
    private static final long TIMESTAMP = 48L;

    private Data data;
    private Date current;

    @Before
    public void setUp() {
        this.current = new Date(48);
        this.data = create();
    }

    @Test
    public void smoke() throws ServiceException {
        final FeedItem converted = InstagramClientTools.convert(this.data, this.current);

        assertEquals(CAPTION, converted.title);
        assertEquals(InstagramClientTools.formatDescription(HTTP_DOMAIN_COM_IMAGE_THUMBNAIL, CAPTION), converted.description);
        assertEquals(HTTP_DOMAIN_COM, converted.link);
        assertEquals(HTTP_DOMAIN_COM, converted.gotoLink);
        assertEquals(TIMESTAMP * 1000, converted.date.getTime());
        assertTrue(converted.dateReal);
        assertFalse(converted.guid.isEmpty());
    }

    @Test
    public void longCaptionIsCutForFitTitle() throws ServiceException {
        this.data.caption.text = FeedHeaderBuilderTest.LONG_STRING;

        final FeedItem converted = InstagramClientTools.convert(this.data, this.current);

        assertTrue(converted.title.length() <= FeedItem.MAX_TITLE_LENGTH);
    }

    @Test
    public void longCaptionIsCutForFitDescription() throws ServiceException {
        this.data.caption.text = FeedHeaderBuilderTest.LONG_STRING;

        final FeedItem converted = InstagramClientTools.convert(this.data, this.current);

        assertTrue(converted.description.length() <= FeedItem.MAX_DESCRIPTION_LENGTH);
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
    public void whenDataTypeIsNullThenErrorOccurs() {
        this.data.type = null;
        assertConversionError(ErrorCode.INSTAGRAM_BAD_DATA_TYPE);
    }

    @Test
    public void whenDataTypeBadThenErrorOccurs() {
        this.data.type = "&*(&bad";
        assertConversionError(ErrorCode.INSTAGRAM_BAD_DATA_TYPE);
    }

    @Test
    public void whenNoImagesThenErrorOccurs() {
        this.data.images = null;

        assertConversionError(ErrorCode.INSTAGRAM_NO_IMAGES);
    }

    @Test
    public void whenAllImagesUrlAreNullThenErrorOccurs() {
        this.data.images.thumbnail.url = null;
        this.data.images.low_resolution.url = null;
        this.data.images.standard_resolution.url = null;

        assertConversionError(ErrorCode.INSTAGRAM_NO_IMAGES);
    }

    @Test
    public void whenAllImagesUrlAreEmptyThenErrorOccurs() {
        this.data.images.thumbnail.url = "";
        this.data.images.low_resolution.url = "";
        this.data.images.standard_resolution.url = "";

        assertConversionError(ErrorCode.INSTAGRAM_NO_IMAGES);
    }

    @Test
    public void whenAllImagesUrlAreInvalidThenErrorOccurs() {
        this.data.images.thumbnail.url = "*";
        this.data.images.low_resolution.url = "*";
        this.data.images.standard_resolution.url = "*";

        assertConversionError(ErrorCode.INSTAGRAM_NO_IMAGES);
    }

    @Test
    public void whenThereAreNoValidImagesUrlThenErrorOccurs() {
        this.data.images.thumbnail.url = "null";
        this.data.images.low_resolution.url = "*";
        this.data.images.standard_resolution.url = "";

        assertConversionError(ErrorCode.INSTAGRAM_NO_IMAGES);
    }

    @Test
    public void whenDataCaptionIsNullThenDefaultIsUsed() throws ServiceException {
        this.data.caption = null;

        final FeedItem converted = InstagramClientTools.convert(this.data, this.current);

        assertEquals(InstagramClientTools.NO_DESCRIPTION, converted.title);
    }

    @Test
    public void whenDataCaptionIsEmptyThenDefaultIsUsed() throws ServiceException {
        this.data.caption.text = " ";

        final FeedItem converted = InstagramClientTools.convert(this.data, this.current);

        assertEquals(InstagramClientTools.NO_DESCRIPTION, converted.title);
    }

    @Test
    public void whenDateIsNullThenCurrentIsUsed() throws ServiceException {
        this.data.created_time = null;

        final FeedItem converted = InstagramClientTools.convert(this.data, this.current);

        assertEquals(this.current, converted.date);
        assertFalse(converted.dateReal);
    }

    @Test
    public void whenDateFromFarPastThenCurrentIsUsedInstead() throws ServiceException {
        this.data.created_time = TIMESTAMP - FeedItem.FIFTY_YEARS - FeedItem.TWENTY_FOUR_HOURS;

        final FeedItem converted = InstagramClientTools.convert(this.data, this.current);

        assertEquals(this.current, converted.date);
        assertFalse(converted.dateReal);
    }

    @Test
    public void whenDateFromFarFutureThenCurrentIsUsedInstead() throws ServiceException {
        this.data.created_time = TIMESTAMP + FeedItem.FIFTY_YEARS;

        final FeedItem converted = InstagramClientTools.convert(this.data, this.current);

        assertEquals(this.current, converted.date);
        assertFalse(converted.dateReal);
    }

    private void assertConversionError(final ErrorCode errorCode) {

        try {
            InstagramClientTools.convert(this.data, this.current);

            fail();
        } catch (ServiceException exception) {
            assertEquals(errorCode, exception.getError().code);
        }
    }

    public static Data create() {
        Data data = new Data();

        Caption caption = new Caption();
        caption.text = CAPTION;

        Images images = new Images();

        Content imageLowResolution = content(HTTP_DOMAIN_COM_IMAGE_LOW_RESOLUTION, 48, 48);
        Content imageThumbnail = content(HTTP_DOMAIN_COM_IMAGE_THUMBNAIL, 48, 48);
        Content imageStandardResolution = content(HTTP_DOMAIN_COM_IMAGE_STANDARD_RESOLUTION, 48, 48);

        images.low_resolution = imageLowResolution;
        images.standard_resolution = imageStandardResolution;
        images.thumbnail = imageThumbnail;


        data.link = HTTP_DOMAIN_COM;
        data.type = "image";
        data.created_time = TIMESTAMP;
        data.caption = caption;
        data.images = images;

        return data;
    }

    private static Content content(final String url, final int width, final int height) {
        final Content content = new Content();

        content.url = url;
        content.width = width;
        content.height = height;

        return content;
    }

}
