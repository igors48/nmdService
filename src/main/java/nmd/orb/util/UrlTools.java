package nmd.orb.util;

import java.net.URI;
import java.net.URL;

import static nmd.orb.util.Assert.assertStringIsValid;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.isValidUrl;
import static nmd.orb.util.Parameter.notNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 31.08.13
 */
public final class UrlTools {

    public static String deleteLastSlash(final String feedUrl) {
        assertStringIsValid(feedUrl);

        String lowered = feedUrl.toLowerCase();

        while (lowered.endsWith("/")) {
            lowered = lowered.substring(0, lowered.length() - 1);
        }

        while (lowered.endsWith("\\")) {
            lowered = lowered.substring(0, lowered.length() - 1);
        }

        return lowered;
    }

    public static String getBaseLink(final String url) {

        try {
            final URL uri = new URL(url);

            return uri.getProtocol() + "://" + uri.getAuthority();
        } catch (Exception e) {
            return url;
        }
    }

    public static String normalize(final String base, final String url) {
        guard(isValidUrl(base));
        guard(notNull(url));

        try {
            final URI uri = new URI(url);

            return uri.isAbsolute() ? url : base + addLeadingSlash(url);
        } catch (Exception e) {
            return url;
        }
    }

    private static String addLeadingSlash(final String url) {
        return url.startsWith("/") ? url : "/" + url;
    }

    private UrlTools() {
        // empty
    }

}
