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

    public static String trim(final String string) {
        return string == null ? "" : string.trim();
    }

    public static String trimOrUse(final String string, final String substitute) {
        guard(notNull(substitute));

        final String trimmed = trim(string);

        return trimmed.isEmpty() ? substitute : trimmed;
    }

}
