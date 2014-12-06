package unit.feed.controller.importer;

import nmd.orb.error.ErrorCode;
import nmd.orb.error.ServiceException;
import nmd.orb.services.importer.CategoryImportContext;
import nmd.orb.services.importer.CategoryImportTaskStatus;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by igor on 06.12.2014.
 */
public class CategoryImportContextConversionTest extends PayloadConversionTestBase {

    @Test
    public void smoke() throws ServiceException {
        final CategoryImportContext context = CategoryImportContext.convert(this.categoryPayload, this.feedHeaderPayloads, TRIES_COUNT);

        assertEquals(NAME, context.getCategoryName());
        assertEquals(CategoryImportTaskStatus.CATEGORY_CREATE, context.getStatus());
        assertEquals(this.feedHeaderPayloads.size(), context.getFeedImportContexts().size());
    }

    @Test
    public void whenCategoryNameInvalidThenErrorOccurs() {
        this.categoryPayload.name = "";

        try {
            CategoryImportContext.convert(this.categoryPayload, this.feedHeaderPayloads, TRIES_COUNT);

            fail();
        } catch (ServiceException exception) {
            assertEquals(ErrorCode.INVALID_CATEGORY_NAME, exception.getError().code);
        }
    }

}
