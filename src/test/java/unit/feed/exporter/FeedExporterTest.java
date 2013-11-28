package unit.feed.exporter;

import nmd.rss.collector.exporter.FeedExporter;
import nmd.rss.collector.exporter.FeedExporterException;
import nmd.rss.collector.feed.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 18.05.13
 */
public class FeedExporterTest {

    private static final String RSS_FEED_URL = "http://www.3dnews.ru/news/rss";
    private static final String HEADER_TITLE = "headerTitle";
    private static final String HEADER_DESCRIPTION = "headerDescription";
    private static final String HEADER_LINK = "headerLink";

    @Test
    public void roundtrip() throws FeedExporterException, FeedParserException {
        final FeedHeader header = new FeedHeader(UUID.randomUUID(), RSS_FEED_URL, HEADER_TITLE, HEADER_DESCRIPTION, HEADER_LINK);
        final FeedItem first = new FeedItem("firstTitle", "firstDescription", "firstLink", new Date(48), "firstLink");
        final FeedItem second = new FeedItem("secondTitle", "secondDescription", "secondLink", new Date(50), "secondLink");
        final List<FeedItem> items = new ArrayList<>();
        items.add(first);
        items.add(second);

        String exported = FeedExporter.export(header, items);

        Feed parsed = FeedParser.parse(RSS_FEED_URL, exported);

        assertEquals(HEADER_TITLE, parsed.header.title);
        assertEquals(HEADER_DESCRIPTION, parsed.header.description);
        assertEquals(HEADER_LINK, parsed.header.link);

        assertEquals(items.size(), parsed.items.size());

        assertTrue(first.equalsExculdeGuid(parsed.items.get(0)));
        assertTrue(second.equalsExculdeGuid(parsed.items.get(1)));
    }

}
