package unit.feed.exporter;

import com.sun.syndication.io.FeedException;
import nmd.orb.error.ServiceException;
import nmd.orb.exporter.FeedExporter;
import nmd.orb.exporter.FeedExporterException;
import nmd.orb.feed.Feed;
import nmd.orb.feed.FeedHeader;
import nmd.orb.feed.FeedItem;
import nmd.orb.sources.rss.FeedParser;
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
    private static final String HEADER_LINK = "http://domain.com/headerLink";

    @Test
    public void roundtrip() throws FeedExporterException, FeedException, ServiceException {
        final FeedHeader header = new FeedHeader(UUID.randomUUID(), RSS_FEED_URL, HEADER_TITLE, HEADER_DESCRIPTION, HEADER_LINK);

        final FeedItem first = new FeedItem("firstTitle", "firstDescription", "http://domain.com/firstLink", "http://domain.com/firstGotoLink", new Date(48), true, "http://domain.com/firstLink");
        final FeedItem second = new FeedItem("secondTitle", "secondDescription", "http://domain.com/secondLink", "http://domain.com/secondGotoLink", new Date(50), true, "http://domain.com/secondLink");

        final FeedItem firstExpected = new FeedItem("firstTitle", "firstDescription", "http://domain.com/firstGotoLink", "http://domain.com/firstGotoLink", new Date(48), true, "http://domain.com/firstLink");
        final FeedItem secondExpected = new FeedItem("secondTitle", "secondDescription", "http://domain.com/secondGotoLink", "http://domain.com/secondGotoLink", new Date(50), true, "http://domain.com/secondLink");

        final List<FeedItem> items = new ArrayList<>();
        items.add(first);
        items.add(second);

        String exported = FeedExporter.export(header, items);

        Feed parsed = FeedParser.parse(RSS_FEED_URL, exported);

        assertEquals(HEADER_TITLE, parsed.header.title);
        assertEquals(HEADER_DESCRIPTION, parsed.header.description);
        assertEquals(HEADER_LINK, parsed.header.link);

        assertEquals(items.size(), parsed.items.size());

        assertTrue(firstExpected.equalsExcludeGuid(parsed.items.get(0)));
        assertTrue(secondExpected.equalsExcludeGuid(parsed.items.get(1)));
    }

}
