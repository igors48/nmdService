package nmd.orb.http.wrappers;

import nmd.orb.error.ServiceException;
import nmd.orb.http.tools.ResponseBody;
import nmd.orb.services.ContentFilterService;

import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.format;
import static nmd.orb.http.tools.ResponseBody.createErrorJsonResponse;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.isValidUrl;
import static nmd.orb.util.Parameter.notNull;

/**
 * @author : igu
 */
public class ContentFilterServiceWrapperImpl implements ContentFilterServiceWrapper {

    private static final Logger LOGGER = Logger.getLogger(ContentFilterServiceWrapperImpl.class.getName());

    private final ContentFilterService contentFilterService;

    public ContentFilterServiceWrapperImpl(final ContentFilterService contentFilterService) {
        guard(notNull(contentFilterService));
        this.contentFilterService = contentFilterService;
    }

    @Override
    public ResponseBody filter(final String link) {
        guard(isValidUrl(link));

        try {
            final String filteredContent = this.contentFilterService.filter(link);

            return null;
        } catch (ServiceException exception) {
            LOGGER.log(Level.SEVERE, format("Error filtering content from [ %s ]", link), exception);

            return createErrorJsonResponse(exception);
        }
    }

}
