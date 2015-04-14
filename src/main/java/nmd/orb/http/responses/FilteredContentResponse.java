package nmd.orb.http.responses;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * @author : igu
 */
public class FilteredContentResponse extends SuccessResponse {

    public String content;

    private FilteredContentResponse() {
        // empty
    }

    public static FilteredContentResponse convert(final String content) {
        guard(notNull(content));

        final FilteredContentResponse response = new FilteredContentResponse();

        response.content = content;

        return response;
    }

}
