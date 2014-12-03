package unit.feed.controller.stub;

import nmd.orb.error.ServiceException;
import nmd.orb.services.importer.CategoriesServiceAdapter;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

/**
 * @author : igu
 */
public class CategoriesServiceAdapterStub implements CategoriesServiceAdapter {

    public static final String CATEGORY_ID = UUID.randomUUID().toString();

    private int callCount = 0;
    private String categoryName = "";

    @Override
    public String addCategory(final String name) throws ServiceException {
        ++this.callCount;

        this.categoryName = name;

        return CATEGORY_ID;
    }

    public void assertCalledOnce(final String name) {
        assertEquals(1, this.callCount);
        assertEquals(name, this.categoryName);
    }

}
