package nmd.rss.http.requests;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 23.03.2014
 */
public class AddFeedRequest {

    public String feedUrl;
    public String categoryId;

    private AddFeedRequest() {
        // empty
    }

    public static AddFeedRequest create(final String feedUrl, final String categoryId) {
        final AddFeedRequest addFeedRequest = new AddFeedRequest();

        addFeedRequest.feedUrl = feedUrl;
        addFeedRequest.categoryId = categoryId;

        return addFeedRequest;
    }

}
