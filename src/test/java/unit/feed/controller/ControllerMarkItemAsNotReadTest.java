package unit.feed.controller;

import nmd.orb.error.ServiceException;
import nmd.orb.feed.FeedHeader;
import nmd.orb.feed.FeedItem;
import nmd.orb.reader.Category;
import nmd.orb.reader.ReadFeedItems;
import org.junit.Test;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date: 26.11.13
 */
public class ControllerMarkItemAsNotReadTest extends AbstractControllerTestBase {

    private static final String NOT_EXISTS_ID = "not_exists_id";

    @Test(expected = ServiceException.class)
    public void whenTryToMarkItemOfNotExistsFeedThenErrorReturns() throws ServiceException {
        this.readsService.markItemAsNotRead(UUID.randomUUID(), UUID.randomUUID().toString());
    }

    @Test
    public void whenItemIdDoesNotExistInFeedItemsIdListThenItDoesNotStore() throws ServiceException {
        final FeedItem feedItem = create(1);
        final FeedHeader feedHeader = createSampleFeed(feedItem);

        this.readsService.markItemAsNotRead(feedHeader.id, NOT_EXISTS_ID);

        final Set<String> readItems = this.readFeedItemsRepositoryStub.load(feedHeader.id).readItemIds;

        assertTrue(readItems.isEmpty());
    }

    @Test
    public void whenItemIdMarkedAsNotReadThenItIsRemovedFromReadItemList() throws ServiceException {
        final FeedItem first = create(1);
        final FeedItem second = create(2);

        final FeedHeader feedHeader = createSampleFeed(first, second);

        this.readsService.markItemAsRead(feedHeader.id, first.guid);
        this.readsService.markItemAsRead(feedHeader.id, second.guid);

        this.readsService.markItemAsNotRead(feedHeader.id, first.guid);

        final Set<String> readItems = this.readFeedItemsRepositoryStub.load(feedHeader.id).readItemIds;

        assertEquals(1, readItems.size());
        assertTrue(readItems.contains(second.guid));
    }

    @Test
    public void whenItemIdMarkedAsNotReadThenReadLaterItemListNotChanged() throws ServiceException {
        final FeedItem first = create(1);
        final FeedItem second = create(2);

        final FeedHeader feedHeader = createSampleFeed(first, second);

        this.readsService.markItemAsRead(feedHeader.id, first.guid);
        this.readsService.toggleReadLaterItemMark(feedHeader.id, second.guid);

        this.readsService.markItemAsNotRead(feedHeader.id, first.guid);

        final Set<String> readLaterItems = this.readFeedItemsRepositoryStub.load(feedHeader.id).readLaterItemIds;

        assertEquals(1, readLaterItems.size());
        assertTrue(readLaterItems.contains(second.guid));
    }

    @Test
    public void whenNotActualReadItemFoundWhileMarkingThenTheyRemoved() throws ServiceException {
        final FeedItem feedItem = create(1);
        final FeedHeader feedHeader = createSampleFeed(feedItem);

        this.readFeedItemsRepositoryStub.store(new ReadFeedItems(feedHeader.id, new Date(), new HashSet<String>() {{
            add(NOT_EXISTS_ID);
        }}, new HashSet<String>(), Category.MAIN_CATEGORY_ID));

        this.readsService.markItemAsNotRead(feedHeader.id, feedItem.guid);

        final Set<String> readItems = this.readFeedItemsRepositoryStub.load(feedHeader.id).readItemIds;

        assertFalse(readItems.contains(NOT_EXISTS_ID));
    }

    @Test
    public void whenItemIdMarkedAsNotReadThenLastUpdateTimeSetToYoungestReadTimestamp() throws ServiceException {
        final FeedItem first = create(1);
        final FeedItem second = create(2);
        final FeedItem third = create(3);

        final FeedHeader feedHeader = createSampleFeed(first, second, third);

        this.readsService.markItemAsRead(feedHeader.id, second.guid);
        this.readsService.markItemAsRead(feedHeader.id, third.guid);

        this.readsService.markItemAsNotRead(feedHeader.id, third.guid);

        final Date lastUpdate = this.readFeedItemsRepositoryStub.load(feedHeader.id).lastUpdate;

        assertEquals(second.date, lastUpdate);
    }

    @Test
    public void whenItemIdMarkedAsNotReadAndNoMoreReadItemsThenLastUpdateTimeSetToEpoch() throws ServiceException {
        final FeedItem first = create(1);

        final FeedHeader feedHeader = createSampleFeed(first);

        this.readsService.markItemAsRead(feedHeader.id, first.guid);
        this.readsService.markItemAsNotRead(feedHeader.id, first.guid);

        final Date lastUpdate = this.readFeedItemsRepositoryStub.load(feedHeader.id).lastUpdate;

        assertEquals(0, lastUpdate.getTime());
    }

}
