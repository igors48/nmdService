package unit.feed.controller;

import nmd.rss.collector.feed.FeedHeader;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 21.06.13
 */
public class ControllerGetFeedHeadersTest extends AbstractControllerTest {

    @Test
    public void whenFeedsAddedThenTheyReturnInList() throws Exception {
        final UUID firstFeedId = addValidFirstRssFeed();
        final UUID secondFeedId = addValidSecondRssFeed();

        final List<FeedHeader> feeds = this.controlService.getFeedHeaders();

        assertEquals(2, feeds.size());

        assertEquals(firstFeedId, feeds.get(0).id);
        assertEquals(secondFeedId, feeds.get(1).id);
    }

}
