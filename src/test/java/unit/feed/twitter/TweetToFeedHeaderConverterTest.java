package unit.feed.twitter;

import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.sources.twitter.entities.Urls;
import org.junit.Test;

import java.util.ArrayList;

import static nmd.rss.collector.feed.FeedHeader.MAX_DESCRIPTION_AND_TITLE_LENGTH;
import static nmd.rss.sources.twitter.TweetConversionTools.convertToHeader;
import static org.junit.Assert.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 28.02.14
 */
public class TweetToFeedHeaderConverterTest extends AbstractTweetConverterTestBase {

    private static final String TWITTER_URL = "http://domain.com/twitter_url";
    private static final String LONG_STRING = "012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789";

    @Test
    public void allFeedHeaderFieldsWereTrimmedAndAssignedProperly() {
        final FeedHeader feedHeader = convertToHeader(TWITTER_URL, this.tweet);

        assertEquals(TWITTER_URL, feedHeader.feedLink);
        assertEquals(USER_DESCRIPTION, feedHeader.description);
        assertEquals(USER_NAME, feedHeader.title);
        assertEquals(FIRST_USER_ENTITY_EXPANDED_URL, feedHeader.link);
        assertNotNull(feedHeader.id);
    }

    @Test
    public void whenUserIsNullThenNullReturns() {
        this.tweet.setUser(null);

        assertNull(convertToHeader(TWITTER_URL, this.tweet));
    }

    @Test
    public void whenUserNameNullDescriptionNullThenNullReturns() {
        this.tweet.getUser().setName(null);
        this.tweet.getUser().setDescription(null);

        assertNull(convertToHeader(TWITTER_URL, this.tweet));
    }

    @Test
    public void whenUserNameEmptyDescriptionNullThenNullReturns() {
        this.tweet.getUser().setName("");
        this.tweet.getUser().setDescription(null);

        assertNull(convertToHeader(TWITTER_URL, this.tweet));
    }

    @Test
    public void whenUserNameEmptyDescriptionEmptyThenNullReturns() {
        this.tweet.getUser().setName("");
        this.tweet.getUser().setDescription("");

        assertNull(convertToHeader(TWITTER_URL, this.tweet));
    }

    @Test
    public void whenUserNameNullDescriptionEmptyThenNullReturns() {
        this.tweet.getUser().setName(null);
        this.tweet.getUser().setDescription("");

        assertNull(convertToHeader(TWITTER_URL, this.tweet));
    }

    @Test
    public void whenUserNameExistDescriptionNullThenNameCopiesToDescription() {
        this.tweet.getUser().setDescription(null);

        final FeedHeader feedHeader = convertToHeader(TWITTER_URL, this.tweet);
        assertEquals(USER_NAME, feedHeader.description);
    }

    @Test
    public void whenUserNameExistDescriptionEmptyThenNameCopiesToDescription() {
        this.tweet.getUser().setDescription("");

        final FeedHeader feedHeader = convertToHeader(TWITTER_URL, this.tweet);
        assertEquals(USER_NAME, feedHeader.description);
    }

    @Test
    public void whenUserNameNullDescriptionExistsThenDescriptionCopiesToTitle() {
        this.tweet.getUser().setName(null);

        final FeedHeader feedHeader = convertToHeader(TWITTER_URL, this.tweet);
        assertEquals(USER_DESCRIPTION, feedHeader.title);
    }

    @Test
    public void whenUserNameEmptyDescriptionExistsThenDescriptionCopiesToTitle() {
        this.tweet.getUser().setName("");

        final FeedHeader feedHeader = convertToHeader(TWITTER_URL, this.tweet);
        assertEquals(USER_DESCRIPTION, feedHeader.title);
    }

    @Test
    public void whenUserEntitiesNullThenTwitterUrlReturns() {
        this.tweet.getUser().setEntities(null);

        assertEquals(TWITTER_URL, convertToHeader(TWITTER_URL, this.tweet).feedLink);
    }

    @Test
    public void whenUrlInUserEntitiesNullThenTwitterUrlReturns() {
        this.tweet.getUser().getEntities().setUrl(null);

        assertEquals(TWITTER_URL, convertToHeader(TWITTER_URL, this.tweet).feedLink);
    }

    @Test
    public void whenUrlListInUserEntitiesNullThenTwitterUrlReturns() {
        this.tweet.getUser().getEntities().getUrl().setUrls(null);

        assertEquals(TWITTER_URL, convertToHeader(TWITTER_URL, this.tweet).feedLink);
    }

    @Test
    public void whenUrlListInUserEntitiesEmptyThenTwitterUrlReturns() {
        this.tweet.getUser().getEntities().getUrl().setUrls(new ArrayList<Urls>());

        assertEquals(TWITTER_URL, convertToHeader(TWITTER_URL, this.tweet).feedLink);
    }

    @Test
    public void whenFirstUrlsElementInUserEntitiesNullThenTwitterUrlReturns() {
        this.tweet.getUser().getEntities().getUrl().getUrls().set(0, null);

        assertEquals(TWITTER_URL, convertToHeader(TWITTER_URL, this.tweet).feedLink);
    }

    @Test
    public void whenFirstExpandedUrlInUserEntitiesNullThenTwitterUrlReturns() {
        this.tweet.getUser().getEntities().getUrl().getUrls().get(0).setExpanded_url(null);

        assertEquals(TWITTER_URL, convertToHeader(TWITTER_URL, this.tweet).feedLink);
    }

    @Test
    public void whenFirstExpandedUrlInUserEntitiesEmptyThenTwitterUrlReturns() {
        this.tweet.getUser().getEntities().getUrl().getUrls().get(0).setExpanded_url("");

        assertEquals(TWITTER_URL, convertToHeader(TWITTER_URL, this.tweet).feedLink);
    }

    @Test
    public void longUserNameAndDescriptionAreCut() {
        this.tweet.getUser().setName(LONG_STRING);
        this.tweet.getUser().setDescription(LONG_STRING);

        final FeedHeader header = convertToHeader(TWITTER_URL, this.tweet);

        assertTrue(header.description.length() == MAX_DESCRIPTION_AND_TITLE_LENGTH);
        assertTrue(header.title.length() == MAX_DESCRIPTION_AND_TITLE_LENGTH);
    }

}
