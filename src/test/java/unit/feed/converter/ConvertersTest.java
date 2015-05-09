package unit.feed.converter;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import nmd.orb.collector.scheduler.FeedUpdateTask;
import nmd.orb.feed.FeedHeader;
import nmd.orb.feed.FeedItem;
import nmd.orb.gae.repositories.converters.*;
import nmd.orb.gae.repositories.converters.helpers.FeedItemHelper;
import nmd.orb.reader.Category;
import nmd.orb.reader.ReadFeedItems;
import nmd.orb.services.change.Event;
import nmd.orb.services.export.Change;
import nmd.orb.services.importer.*;
import org.junit.Test;
import unit.feed.controller.importer.CategoryImportContextTest;
import unit.feed.controller.importer.FeedImportContextTest;
import unit.feed.controller.importer.ImportJobContextTest;

import java.util.*;

import static nmd.orb.reader.Category.MAIN_CATEGORY_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date: 18.10.13
 */
public class ConvertersTest {

    private static final Key SAMPLE_KEY = KeyFactory.stringToKey("ag9zfnJzcy1jb2xsZWN0b3JyHQsSEEZlZWRIZWFkZXJFbnRpdHkYgICAgIi0vwgM");

    private static final FeedItem FIRST_FEED_ITEM = new FeedItem("title-first", "description-first", "http://domain.com/link-first", "http://domain.com/link-goto-first", new Date(), false, "guid-first");
    private static final FeedItem SECOND_FEED_ITEM = new FeedItem("title-second", "description-second", "http://domain.com/link-second", "http://domain.com/link-goto-second", new Date(), true, "guid-second");

    private static final List<FeedItem> FEED_LIST = Arrays.asList(FIRST_FEED_ITEM, SECOND_FEED_ITEM);

    private static final String FIRST_READ_ITEM_ID = "first";
    private static final String SECOND_READ_ITEM_ID = "second";
    private static final Set<String> READ_FEED_ITEMS = new HashSet<String>() {{
        add(FIRST_READ_ITEM_ID);
        add(SECOND_READ_ITEM_ID);
    }};

    private static final String FIRST_READ_LATER_ITEM_ID = "first-later";
    private static final String SECOND_READ_LATER_ITEM_ID = "second-later";
    private static final Set<String> READ_LATER_FEED_ITEMS = new HashSet<String>() {{
        add(FIRST_READ_LATER_ITEM_ID);
        add(SECOND_READ_LATER_ITEM_ID);
    }};

    @Test
    public void feedHeaderEntityRoundtrip() {
        final FeedHeader origin = new FeedHeader(UUID.randomUUID(), "http://domain.com/feedLink", "title", "description", "http://domain.com/link");

        final Entity entity = FeedHeaderEntityConverter.convert(origin, SAMPLE_KEY);

        final FeedHeader restored = FeedHeaderEntityConverter.convert(entity);

        assertEquals(origin, restored);
    }

    @Test
    public void feedUpdateTaskEntityRoundtrip() {
        final FeedUpdateTask origin = new FeedUpdateTask(UUID.randomUUID(), 1000);

        final Entity entity = FeedUpdateTaskEntityConverter.convert(origin, SAMPLE_KEY);

        final FeedUpdateTask restored = FeedUpdateTaskEntityConverter.convert(entity);

        assertEquals(origin, restored);
    }

    @Test
    public void feedItemHelperRoundtrip() {
        final FeedItemHelper helper = FeedItemHelper.convert(FIRST_FEED_ITEM);
        final FeedItem restored = FeedItemHelper.convert(helper);

        assertEquals(FIRST_FEED_ITEM, restored);
    }

    @Test
    public void feedItemListRoundtrip() {
        final String converted = FeedItemListConverter.convert(FEED_LIST);
        final List<FeedItem> restored = FeedItemListConverter.convert(converted);

        assertEquals(FEED_LIST.size(), restored.size());
        assertEquals(FIRST_FEED_ITEM, restored.get(0));
        assertEquals(SECOND_FEED_ITEM, restored.get(1));
    }

    @Test
    public void feedItemListEntityRoundtrip() {
        final Entity entity = FeedItemListEntityConverter.convert(SAMPLE_KEY, UUID.randomUUID(), FEED_LIST);

        final List<FeedItem> restored = FeedItemListEntityConverter.convert(entity);

        assertEquals(FEED_LIST.size(), restored.size());
        assertEquals(FIRST_FEED_ITEM, restored.get(0));
        assertEquals(SECOND_FEED_ITEM, restored.get(1));
    }

    @Test
    public void readFeedItemsEntityRoundtrip() {
        final Date date = new Date();
        final UUID feedId = UUID.randomUUID();
        final ReadFeedItems origin = new ReadFeedItems(feedId, date, READ_FEED_ITEMS, READ_LATER_FEED_ITEMS, MAIN_CATEGORY_ID);
        final Entity entity = ReadFeedItemsConverter.convert(SAMPLE_KEY, origin);

        final ReadFeedItems restored = ReadFeedItemsConverter.convert(entity);

        assertEquals(feedId, restored.feedId);
        assertEquals(date, restored.lastUpdate);

        assertEquals(READ_FEED_ITEMS.size(), restored.readItemIds.size());
        assertTrue(restored.readItemIds.contains(FIRST_READ_ITEM_ID));
        assertTrue(restored.readItemIds.contains(SECOND_READ_ITEM_ID));

        assertEquals(READ_LATER_FEED_ITEMS.size(), restored.readLaterItemIds.size());
        assertTrue(restored.readLaterItemIds.contains(FIRST_READ_LATER_ITEM_ID));
        assertTrue(restored.readLaterItemIds.contains(SECOND_READ_LATER_ITEM_ID));

        assertEquals(MAIN_CATEGORY_ID, restored.categoryId);
    }

    @Test
    public void categoryEntityRoundtrip() {
        final UUID uuid = UUID.randomUUID();

        final Category category = new Category(uuid.toString(), "name");

        final Entity entity = CategoryConverter.convert(category, SAMPLE_KEY);

        final Category restored = CategoryConverter.convert(entity);

        assertEquals(category, restored);
    }

    @Test
    public void importJobContextRoundtrip() {
        final FeedImportContext feedImportContext = FeedImportContextTest.create(3, FeedImportTaskStatus.COMPLETED);
        final CategoryImportContext categoryImportContext = CategoryImportContextTest.create(CategoryImportTaskStatus.COMPLETED, feedImportContext);
        final ImportJobContext context = ImportJobContextTest.create(ImportJobStatus.COMPLETED, categoryImportContext);

        final Entity entity = ImportJobContextConverter.convert(context, SAMPLE_KEY);

        final ImportJobContext restored = ImportJobContextConverter.convert(entity);

        assertEquals(context, restored);
    }

    @Test
    public void changeRoundtrip() {
        final Change original = new Change(48, new ArrayList<Event>(), true);
        final Entity entity = ChangeConverter.convert(original, SAMPLE_KEY);
        final Change restored = ChangeConverter.convert(entity);

        assertEquals(original, restored);
    }
}
