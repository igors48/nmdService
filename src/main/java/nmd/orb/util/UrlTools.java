package nmd.orb.util;

import java.net.URL;

import static nmd.orb.util.Assert.assertStringIsValid;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 31.08.13
 */
public final class UrlTools {

    public static String normalizeUrl(final String feedUrl) {
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

    private UrlTools() {
        // empty
    }

}
