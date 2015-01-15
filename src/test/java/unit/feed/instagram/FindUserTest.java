package unit.feed.instagram;

import nmd.orb.error.ErrorCode;
import nmd.orb.error.ServiceError;
import nmd.orb.error.ServiceException;
import nmd.orb.sources.instagram.InstagramClientTools;
import nmd.orb.sources.instagram.entities.Meta;
import nmd.orb.sources.instagram.entities.User;
import nmd.orb.sources.instagram.entities.UserEnvelope;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 12.11.2014
 */
public class FindUserTest {

    private User first;
    private UserEnvelope userEnvelope;

    @Before
    public void setUp() throws Exception {
        Meta meta = new Meta();
        meta.code = "200";

        this.first = create("first");
        User second = create("second");
        User third = create("third");

        List<User> users = new ArrayList<>();
        users.add(this.first);
        users.add(second);
        users.add(third);

        this.userEnvelope = new UserEnvelope();
        this.userEnvelope.meta = meta;
        this.userEnvelope.data = users;
    }

    @Test
    public void ifMetaNullThenErrorOccured() {
        this.userEnvelope.meta = null;

        assertErrorOccured(ErrorCode.INSTAGRAM_NO_META);
    }

    @Test
    public void ifMetaCodeIsNot200ThenErrorOccured() {
        this.userEnvelope.meta.code = "201";

        assertErrorOccured(ErrorCode.INSTAGRAM_WRONG_STATUS_CODE);
    }

    @Test
    public void ifDataIsNullThenErrorOccured() {
        this.userEnvelope.data = null;

        assertErrorOccured(ErrorCode.INSTAGRAM_NO_USERS);
    }

    @Test
    public void ifDataIsEmptyThenErrorOccured() {
        this.userEnvelope.data = new ArrayList<>();

        assertErrorOccured(ErrorCode.INSTAGRAM_NO_USERS);
    }

    @Test
    public void ifUserIsNotFoundThenErrorOccured() {
        assertErrorOccured(ErrorCode.INSTAGRAM_USER_NOT_FOUND);
    }

    @Test
    public void ifUserIsFoundThenUserReturns() throws ServiceException {
        final User user = InstagramClientTools.findUser(createUserName("first"), this.userEnvelope);

        assertEquals(this.first, user);
    }

    private void assertErrorOccured(final ErrorCode expectedErrorCode) {

        try {
            InstagramClientTools.findUser("wrongName", this.userEnvelope);

            fail();
        } catch (ServiceException exception) {
            assertErrorOccured(exception, expectedErrorCode);
        }
    }

    private void assertErrorOccured(final ServiceException exception, final ErrorCode expectedErrorCode) {
        final ServiceError serviceError = exception.getError();
        final ErrorCode errorCode = serviceError.code;

        assertEquals(expectedErrorCode, errorCode);
    }

    public static User create(final String id) {
        final User user = new User();

        user.full_name = id + "full_name";
        user.username = createUserName(id);
        user.id = id;

        return user;
    }

    private static String createUserName(final String id) {
        return id + "username";
    }

}
