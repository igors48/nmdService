package nmd.orb.gae.repositories.converters;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import nmd.orb.collector.feed.FeedHeader;

import java.util.UUID;

import static nmd.orb.collector.util.Assert.assertNotNull;
import static nmd.orb.gae.repositories.datastore.Kind.FEED_HEADER;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 15.10.13
 */
public class FeedHeaderEntityConverter {

    public static final String FEED_LINK = "feedLink";

    private static final String HEADER_ID = "headerId";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String LINK = "link";

    public static Entity convert(final FeedHeader header, final Key feedKey) {
        assertNotNull(header);
        assertNotNull(feedKey);

        final Entity entity = new Entity(FEED_HEADER.value, feedKey);

        entity.setProperty(HEADER_ID, header.id.toString());
        entity.setProperty(FEED_LINK, header.feedLink);
        entity.setProperty(TITLE, header.title);
        entity.setProperty(DESCRIPTION, header.description);
        entity.setProperty(LINK, header.link);

        return entity;
    }

    public static FeedHeader convert(final Entity entity) {
        assertNotNull(entity);

        final UUID id = UUID.fromString((String) entity.getProperty(HEADER_ID));
        final String feedLink = (String) entity.getProperty(FEED_LINK);
        final String title = (String) entity.getProperty(TITLE);
        final String description = (String) entity.getProperty(DESCRIPTION);
        final String link = (String) entity.getProperty(LINK);

        return new FeedHeader(id, feedLink, title, description, link);
    }

    private FeedHeaderEntityConverter() {
        // empty
    }

}
