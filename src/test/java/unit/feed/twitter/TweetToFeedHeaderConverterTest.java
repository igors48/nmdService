package unit.feed.twitter;

import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.collector.twitter.entities.Urls;
import org.junit.Test;

import java.util.ArrayList;

import static nmd.rss.collector.twitter.TweetConversionTools.convertToHeader;
import static org.junit.Assert.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 28.02.14
 */
public class TweetToFeedHeaderConverterTest extends AbstractTweetConverterTestBase {

    private static final String TWITTER_URL = "http://domain.com/twitter_url";

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
    public void whenUserEntitiesNullThenNullReturns() {
        this.tweet.getUser().setEntities(null);

        assertNull(convertToHeader(TWITTER_URL, this.tweet));
    }

    @Test
    public void whenUrlInUserEntitiesNullThenNullReturns() {
        this.tweet.getUser().getEntities().setUrl(null);

        assertNull(convertToHeader(TWITTER_URL, this.tweet));
    }

    @Test
    public void whenUrlListInUserEntitiesNullThenNullReturns() {
        this.tweet.getUser().getEntities().getUrl().setUrls(null);

        assertNull(convertToHeader(TWITTER_URL, this.tweet));
    }

    @Test
    public void whenUrlListInUserEntitiesEmptyThenNullReturns() {
        this.tweet.getUser().getEntities().getUrl().setUrls(new ArrayList<Urls>());

        assertNull(convertToHeader(TWITTER_URL, this.tweet));
    }

    @Test
    public void whenFirstUrlsElementInUserEntitiesNullThenNullReturns() {
        this.tweet.getUser().getEntities().getUrl().getUrls().set(0, null);

        assertNull(convertToHeader(TWITTER_URL, this.tweet));
    }

    @Test
    public void whenFirstExpandedUrlInUserEntitiesNullThenNullReturns() {
        this.tweet.getUser().getEntities().getUrl().getUrls().get(0).setExpanded_url(null);

        assertNull(convertToHeader(TWITTER_URL, this.tweet));
    }

    @Test
    public void whenFirstExpandedUrlInUserEntitiesEmptyThenNullReturns() {
        this.tweet.getUser().getEntities().getUrl().getUrls().get(0).setExpanded_url("");

        assertNull(convertToHeader(TWITTER_URL, this.tweet));
    }


}
