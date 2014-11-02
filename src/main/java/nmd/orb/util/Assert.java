package nmd.orb.util;

import static nmd.orb.util.Parameter.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 28.04.13
 */
public final class Assert {

    public static void assertStringIsValid(final String value) {
        guard(isValidString(value));
    }

    public static void assertPositive(final long value) {
        guard(isPositive(value));
    }

    public static void assertNotNull(final Object value) {
        guard(notNull(value));
    }

    public static void assertValidUrl(final String value) {
        guard(isValidUrl(value));
    }

    public static void guard(final boolean value) {

        if (!value) {
            throw new IllegalArgumentException();
        }
    }

    private Assert() {
        // empty
    }
}
