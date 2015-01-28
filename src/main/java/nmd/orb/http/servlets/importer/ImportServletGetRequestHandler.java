package nmd.orb.http.servlets.importer;

import nmd.orb.http.Handler;
import nmd.orb.http.tools.ResponseBody;
import nmd.orb.http.wrappers.ImportServiceWrapper;

import java.util.List;
import java.util.Map;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Created by igor on 27.11.2014.
 */
public class ImportServletGetRequestHandler implements Handler {

    private final ImportServiceWrapper importServiceWrapper;

    public ImportServletGetRequestHandler(final ImportServiceWrapper importServiceWrapper) {
        guard(notNull(importServiceWrapper));
        this.importServiceWrapper = importServiceWrapper;
    }

    //GET -- get import job status
    @Override
    public ResponseBody handle(final List<String> elements, final Map<String, String> parameters, final String body) {
        guard(notNull(elements));
        guard(notNull(parameters));
        guard(notNull(body));

        return this.importServiceWrapper.status();
    }

}
