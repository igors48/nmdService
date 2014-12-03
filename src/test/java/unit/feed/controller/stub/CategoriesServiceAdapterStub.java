package unit.feed.controller.stub;

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

    private boolean throwException = false;

    @Override
    public String createCategory(final String name) {
        ++this.callCount;

        if (this.throwException) {
            throw new RuntimeException();
        }

        this.categoryName = name;

        return CATEGORY_ID;
    }

    public void assertCalledOnce(final String name) {
        assertEquals(1, this.callCount);
        assertEquals(name, this.categoryName);
    }

    public void setThrowException(final boolean throwException) {
        this.throwException = throwException;
    }

}
