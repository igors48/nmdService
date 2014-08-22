package nmd.rss.http.responses;

import static nmd.rss.collector.util.Assert.guard;
import static nmd.rss.collector.util.Parameter.isValidString;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.06.13
 */
public class SuccessMessageResponse extends SuccessResponse {

    public String message;

    private SuccessMessageResponse() {
        // empty
    }

    public static SuccessMessageResponse create(final String message) {
        guard(isValidString(message));

        final SuccessMessageResponse result = new SuccessMessageResponse();

        result.message = message;

        return result;
    }

}
