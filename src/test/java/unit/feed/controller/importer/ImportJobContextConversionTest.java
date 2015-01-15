package unit.feed.controller.importer;

import nmd.orb.error.ServiceException;
import nmd.orb.services.importer.ImportJobContext;
import nmd.orb.services.importer.ImportJobStatus;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by igor on 06.12.2014.
 */
public class ImportJobContextConversionTest extends PayloadConversionTestBase {

    @Test
    public void smoke() throws ServiceException {
        final ImportJobContext context = ImportJobContext.convert(this.exportReportPayloads, TRIES_COUNT);

        assertEquals(ImportJobStatus.STOPPED, context.getStatus());
        assertEquals(this.exportReportPayloads.size(), context.getContexts().size());
    }

}
