package unit.feed.instagram;

import nmd.orb.feed.FeedHeader;
import nmd.orb.sources.instagram.InstagramClientTools;
import nmd.orb.sources.instagram.entities.User;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by igor on 15.11.2014.
 */
public class UserConversionTest {

    private static final String LINK = "http://domain.com";

    @Test
    public void smoke() {
        final User user = FindUserTest.create("user");

        final FeedHeader header = InstagramClientTools.convert(LINK, user);

        assertNotNull(header.id);
        assertEquals(LINK, header.feedLink);
        assertEquals(LINK, header.link);
        assertEquals(user.full_name, header.title);
        assertEquals(user.full_name, header.description);
    }

    @Test
    public void whenUserFullNameIsEmptyThenUsernameIsUsed() {
        final User user = FindUserTest.create("user");
        user.full_name = "";

        final FeedHeader header = InstagramClientTools.convert(LINK, user);

        assertEquals(user.username, header.title);
        assertEquals(user.username, header.description);
    }

    @Test
    public void whenUserFullNameIsNullThenUsernameIsUsed() {
        final User user = FindUserTest.create("user");
        user.full_name = null;

        final FeedHeader header = InstagramClientTools.convert(LINK, user);

        assertEquals(user.username, header.title);
        assertEquals(user.username, header.description);
    }

}
