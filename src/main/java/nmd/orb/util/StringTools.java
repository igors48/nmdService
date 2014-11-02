package nmd.orb.util;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.isPositive;
import static nmd.orb.util.Parameter.notNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 29.03.2014
 */
public final class StringTools {

    public static String cutTo(final String string, final int size) {
        guard(notNull(string));
        guard(isPositive(size));

        return string.length() > size ? string.substring(0, size) : string;
    }

}
