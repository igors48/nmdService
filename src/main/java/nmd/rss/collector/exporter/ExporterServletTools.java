package nmd.rss.collector.exporter;

import nmd.rss.collector.feed.FeedHeader;
import nmd.rss.collector.feed.FeedItem;
import nmd.rss.collector.updater.FeedService;
import nmd.rss.collector.updater.FeedServiceException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.collector.util.CloseableTools.close;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 18.05.13
 */
public final class ExporterServletTools {

    private static final Logger LOGGER = Logger.getLogger(ExporterServletTools.class.getName());

    public static String readStream(final InputStream _stream) {
        assertNotNull(_stream);

        final StringBuilder result = new StringBuilder();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(_stream));

        String line;

        try {

            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException e) {
            // empty
        } finally {
            close(reader);
        }

        return result.toString();
    }

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

    public static String exportFeed(final UUID feedId, final FeedService feedService) throws FeedServiceException, FeedExporterException {
        assertNotNull(feedId);
        assertNotNull(feedService);

        final FeedHeader header = feedService.loadHeader(feedId);

        if (header == null) {
            LOGGER.severe(String.format("Can not find feed header for feed id [ %s ]", feedId));

            return "";
        }

        final List<FeedItem> items = feedService.loadItems(feedId);

        if (items == null) {
            LOGGER.severe(String.format("Can not find feed items for feed id [ %s ]", feedId));

            return "";
        }

        final String generated = FeedExporter.export(header, items);

        LOGGER.info(String.format("Feed for feed id [ %s ] generated successfully", feedId));

        return generated;
    }

    private ExporterServletTools() {
        // empty
    }

}
