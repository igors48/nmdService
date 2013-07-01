package nmd.rss.collector.rest.responses;

import static nmd.rss.collector.util.Assert.assertStringIsValid;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.06.13
 */
public class SuccessMessageResponse extends SuccessResponse {

    private String message = "";

    private SuccessMessageResponse() {
        // empty
    }

    public static SuccessMessageResponse create(final String message) {
        assertStringIsValid(message);

        final SuccessMessageResponse result = new SuccessMessageResponse();

        result.message = message;

        return result;
    }

}
