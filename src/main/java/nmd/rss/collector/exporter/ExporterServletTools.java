package nmd.rss.collector.exporter;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 18.05.13
 */
public final class ExporterServletTools {

    private static final Logger LOGGER = Logger.getLogger(ExporterServletTools.class.getName());

    public static UUID parseFeedId(final String pathInfo) {

        if (pathInfo == null || pathInfo.length() < 2) {
            return null;
        }

        try {
            final String data = pathInfo.substring(1);

            return UUID.fromString(data);
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, String.format("Error parse feedId from [ %s ]", pathInfo), exception);

            return null;
        }
    }

    private ExporterServletTools() {
        // empty
    }

}
