package unit.feed.controller.importer;

import nmd.orb.error.ErrorCode;
import nmd.orb.error.ServiceException;
import nmd.orb.http.responses.payload.CategoryPayload;
import nmd.orb.http.responses.payload.FeedHeaderPayload;
import nmd.orb.reader.Category;
import nmd.orb.services.importer.CategoryImportContext;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by igor on 06.12.2014.
 */
public class CategoryImportContextConversionTest {

    private static final int TRIES_COUNT = 3;
    private static final String NAME = "name";

    private CategoryPayload categoryPayload;
    private List<FeedHeaderPayload> feedHeaderPayloads;

    @Before
    public void setUp() {
        final Category name = new Category(UUID.randomUUID().toString(), NAME);
        this.categoryPayload = CategoryPayload.create(name);

        final FeedHeaderPayload feedHeaderPayload = FeedHeaderPayload.create("http://domain.com", UUID.randomUUID().toString(), "title");
        this.feedHeaderPayloads = new ArrayList<>();
        this.feedHeaderPayloads.add(feedHeaderPayload);
    }

    @Test
    public void smoke() throws ServiceException {
        final CategoryImportContext context = CategoryImportContext.convert(this.categoryPayload, this.feedHeaderPayloads, TRIES_COUNT);

        assertEquals(NAME, context.getCategoryName());
        assertEquals(1, context.getFeedImportContexts().size());
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
