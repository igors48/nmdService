package rest;

import nmd.rss.collector.rest.responses.FeedIdResponse;
import org.junit.Test;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 03.12.13
 */
public class ReadFeedTest extends AbstractRestTest {

    @Test
    public void whenFeedJustAddedThenAllOfItItemsAreNotRead() {
        final FeedIdResponse feedIdResponse = addFirstFeed();

        final String response = getReadsReport();

        System.out.println(response);
    }

}
