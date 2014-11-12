package nmd.orb.sources.instagram;

import nmd.orb.sources.instagram.entities.UserEnvelope;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.isValidString;
import static nmd.orb.util.Parameter.notNull;

/**
 * @author : igu
 */
public class InstagramClientTools {

    public static String findUserId(final String userName, final UserEnvelope userEnvelope) {
        guard(isValidString(userName));
        guard(notNull(userEnvelope));

        return "";
    }

}
