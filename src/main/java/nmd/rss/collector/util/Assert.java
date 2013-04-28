package nmd.rss.collector.util;

import static nmd.rss.collector.util.Parameter.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 28.04.13
 */
public final class Assert {

    public static void assertStringIsValid(final String value) {
        assertTrue(isValidString(value));
    }

    public static void assertPositive(final long value) {
        assertTrue(isPositive(value));
    }

    public static void assertNotNull(final Object value) {
        assertTrue(notNull(value));
    }

    public static void assertValidUrl(final String value) {
        assertTrue(isValidUrl(value));
    }

    public static void assertTrue(final boolean value) {

        if (!value) {
            throw new IllegalArgumentException();
        }
    }

    private Assert() {
        // empty
    }
}
