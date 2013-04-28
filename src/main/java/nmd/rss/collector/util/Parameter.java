package nmd.rss.collector.util;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 28.04.13
 */
public final class Parameter {

    public static boolean isValidString(final String value) {
        return value != null && !value.isEmpty();
    }

    public static boolean isPositive(final long value) {
        return value >= 0;
    }

    public static boolean notNull(final Object value) {
        return value != null;
    }

    public static boolean isValidUrl(final String value) {

        try {
            new URI(value);

            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }

    private Parameter() {
        // empty
    }

}
