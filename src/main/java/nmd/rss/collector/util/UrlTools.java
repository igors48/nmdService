package nmd.rss.collector.util;

import static nmd.rss.collector.util.Assert.assertStringIsValid;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 31.08.13
 */
public class UrlTools {

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

    private UrlTools() {
        // empty
    }

}
