package nmd.orb.http.wrappers;

import nmd.orb.http.tools.ResponseBody;
import nmd.orb.services.importer.ImportJobContext;

/**
 * Created by igor on 05.12.2014.
 */
public interface ImportServiceWrapper {

    public ResponseBody schedule(final ImportJobContext context);
    
}
