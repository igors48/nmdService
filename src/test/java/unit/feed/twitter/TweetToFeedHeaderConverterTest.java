package unit.feed.twitter;

import nmd.orb.error.ErrorCode;
import nmd.orb.error.ServiceException;
import nmd.orb.feed.FeedHeader;
import nmd.orb.sources.twitter.entities.Urls;
import org.junit.Test;

import java.util.ArrayList;

import static nmd.orb.feed.FeedHeader.MAX_DESCRIPTION_AND_TITLE_LENGTH;
import static nmd.orb.sources.twitter.TweetConversionTools.convertToHeader;
import static org.junit.Assert.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 28.02.14
 */
public class TweetToFeedHeaderConverterTest extends AbstractTweetConverterTestBase {

    private static final String TWITTER_URL = "http://domain.com/twitter_url";
    private static final String LONG_STRING = "012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789";

    @Test
    public void allFeedHeaderFieldsWereTrimmedAndAssignedProperly() throws ServiceException {
        final FeedHeader feedHeader = convertToHeader(TWITTER_URL, this.tweet);

        assertEquals(TWITTER_URL, feedHeader.feedLink);
        assertEquals(USER_DESCRIPTION, feedHeader.description);
        assertEquals(USER_NAME, feedHeader.title);
        assertEquals(FIRST_USER_ENTITY_EXPANDED_URL, feedHeader.link);
        assertNotNull(feedHeader.id);
    }

    @Test
    public void whenUserIsNullThenErrorReturns() throws ServiceException {
        this.tweet.setUser(null);

        assertConversionError(ErrorCode.TWITTER_NO_USER);
    }

    @Test
    public void whenUserNameNullDescriptionNullThenErrorReturns() throws ServiceException {
        this.tweet.getUser().setName(null);
        this.tweet.getUser().setDescription(null);

        assertConversionError(ErrorCode.TWITTER_EMPTY_USER_NAME_AND_DESCRIPTION);
    }

    @Test
    public void whenUserNameEmptyDescriptionNullThenErrorReturns() throws ServiceException {
        this.tweet.getUser().setName("");
        this.tweet.getUser().setDescription(null);

        assertConversionError(ErrorCode.TWITTER_EMPTY_USER_NAME_AND_DESCRIPTION);
    }

    @Test
    public void whenUserNameEmptyDescriptionEmptyThenErrorReturns() throws ServiceException {
        this.tweet.getUser().setName("");
        this.tweet.getUser().setDescription("");

        assertConversionError(ErrorCode.TWITTER_EMPTY_USER_NAME_AND_DESCRIPTION);
    }

    @Test
    public void whenUserNameNullDescriptionEmptyThenErrorReturns() throws ServiceException {
        this.tweet.getUser().setName(null);
        this.tweet.getUser().setDescription("");

        assertConversionError(ErrorCode.TWITTER_EMPTY_USER_NAME_AND_DESCRIPTION);
    }

    @Test
    public void whenUserNameExistDescriptionNullThenNameCopiesToDescription() throws ServiceException {
        this.tweet.getUser().setDescription(null);

        final FeedHeader feedHeader = convertToHeader(TWITTER_URL, this.tweet);
        assertEquals(USER_NAME, feedHeader.description);
    }

    @Test
    public void whenUserNameExistDescriptionEmptyThenNameCopiesToDescription() throws ServiceException {
        this.tweet.getUser().setDescription("");

        final FeedHeader feedHeader = convertToHeader(TWITTER_URL, this.tweet);
        assertEquals(USER_NAME, feedHeader.description);
    }

    @Test
    public void whenUserNameNullDescriptionExistsThenDescriptionCopiesToTitle() throws ServiceException {
        this.tweet.getUser().setName(null);

        final FeedHeader feedHeader = convertToHeader(TWITTER_URL, this.tweet);
        assertEquals(USER_DESCRIPTION, feedHeader.title);
    }

    @Test
    public void whenUserNameEmptyDescriptionExistsThenDescriptionCopiesToTitle() throws ServiceException {
        this.tweet.getUser().setName("");

        final FeedHeader feedHeader = convertToHeader(TWITTER_URL, this.tweet);
        assertEquals(USER_DESCRIPTION, feedHeader.title);
    }

    @Test
    public void whenUserEntitiesNullThenTwitterUrlReturns() throws ServiceException {
        this.tweet.getUser().setEntities(null);

        assertEquals(TWITTER_URL, convertToHeader(TWITTER_URL, this.tweet).feedLink);
    }

    @Test
    public void whenUrlInUserEntitiesNullThenTwitterUrlReturns() throws ServiceException {
        this.tweet.getUser().getEntities().setUrl(null);

        assertEquals(TWITTER_URL, convertToHeader(TWITTER_URL, this.tweet).feedLink);
    }

    @Test
    public void whenUrlListInUserEntitiesNullThenTwitterUrlReturns() throws ServiceException {
        this.tweet.getUser().getEntities().getUrl().setUrls(null);

        assertEquals(TWITTER_URL, convertToHeader(TWITTER_URL, this.tweet).feedLink);
    }

    @Test
    public void whenUrlListInUserEntitiesEmptyThenTwitterUrlReturns() throws ServiceException {
        this.tweet.getUser().getEntities().getUrl().setUrls(new ArrayList<Urls>());

        assertEquals(TWITTER_URL, convertToHeader(TWITTER_URL, this.tweet).feedLink);
    }

    @Test
    public void whenFirstUrlsElementInUserEntitiesNullThenTwitterUrlReturns() throws ServiceException {
        this.tweet.getUser().getEntities().getUrl().getUrls().set(0, null);

        assertEquals(TWITTER_URL, convertToHeader(TWITTER_URL, this.tweet).feedLink);
    }

    @Test
    public void whenFirstExpandedUrlInUserEntitiesNullThenTwitterUrlReturns() throws ServiceException {
        this.tweet.getUser().getEntities().getUrl().getUrls().get(0).setExpanded_url(null);

        assertEquals(TWITTER_URL, convertToHeader(TWITTER_URL, this.tweet).feedLink);
    }

    @Test
    public void whenFirstExpandedUrlInUserEntitiesEmptyThenTwitterUrlReturns() throws ServiceException {
        this.tweet.getUser().getEntities().getUrl().getUrls().get(0).setExpanded_url("");

        assertEquals(TWITTER_URL, convertToHeader(TWITTER_URL, this.tweet).feedLink);
    }

    @Test
    public void longUserNameAndDescriptionAreCut() throws ServiceException {
        this.tweet.getUser().setName(LONG_STRING);
        this.tweet.getUser().setDescription(LONG_STRING);

        final FeedHeader header = convertToHeader(TWITTER_URL, this.tweet);

        assertTrue(header.description.length() == MAX_DESCRIPTION_AND_TITLE_LENGTH);
        assertTrue(header.title.length() == MAX_DESCRIPTION_AND_TITLE_LENGTH);
    }

    private void assertConversionError(final ErrorCode errorCode) {

        try {
            convertToHeader(TWITTER_URL, this.tweet);

            fail();
        } catch (ServiceException exception) {
            assertEquals(errorCode, exception.getError().code);
        }
    }


}
