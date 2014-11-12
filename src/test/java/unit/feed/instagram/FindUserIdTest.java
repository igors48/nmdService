package unit.feed.instagram;

import nmd.orb.error.ServiceException;
import nmd.orb.sources.instagram.entities.Meta;
import nmd.orb.sources.instagram.entities.UserEnvelope;
import org.junit.Before;
import org.junit.Test;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 12.11.2014
 */
public class FindUserIdTest {

    private Meta meta;
    private UserEnvelope userEnvelope;

    @Before
    public void setUp() throws Exception {
        this.meta = new Meta();
        this.meta.code = "200";

        this.userEnvelope = new UserEnvelope();
        this.userEnvelope.meta = this.meta;
    }

    @Test
    public void ifMetaNullThenNullReturns() throws ServiceException {
        this.userEnvelope.meta = null;

        //final User userId = InstagramClientTools.findUser("userName", this.userEnvelope);

        //assertNull(userId);
    }

}
