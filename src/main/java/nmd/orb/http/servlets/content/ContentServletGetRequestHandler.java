package nmd.orb.http.servlets.content;

import nmd.orb.http.Handler;
import nmd.orb.http.tools.ResponseBody;
import nmd.orb.http.wrappers.ContentFilterServiceWrapper;

import java.util.List;
import java.util.Map;

import static nmd.orb.error.ServiceError.invalidUrl;
import static nmd.orb.http.tools.ResponseBody.createErrorJsonResponse;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.isValidUrl;
import static nmd.orb.util.Parameter.notNull;

/**
 * @author : igu
 */
public class ContentServletGetRequestHandler implements Handler {

    private final ContentFilterServiceWrapper contentFilterServiceWrapper;

    public ContentServletGetRequestHandler(final ContentFilterServiceWrapper contentFilterServiceWrapper) {
        guard(notNull(contentFilterServiceWrapper));
        this.contentFilterServiceWrapper = contentFilterServiceWrapper;
    }

    @Override
    public ResponseBody handle(final List<String> elements, final Map<String, String> parameters, final String body) {
        guard(notNull(elements));
        guard(notNull(parameters));
        guard(notNull(body));

        final String link = parameters.get("link");

        if (!isValidUrl(link)) {
            return createErrorJsonResponse(invalidUrl(link));
        }

        return this.contentFilterServiceWrapper.filter(link);
    }

}
