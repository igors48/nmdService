package unit.feed.controller.stub;

import nmd.orb.error.ServiceException;
import nmd.orb.services.importer.FeedsServiceAdapter;

import java.util.UUID;

import static nmd.orb.error.ServiceError.importJobStartedAlready;
import static org.junit.Assert.assertEquals;

/**
 * @author : igu
 */
public class FeedsServiceAdapterStub implements FeedsServiceAdapter {

    private int callCount = 0;
    private String feedLink = "";
    private String feedTitle = "";
    private String categoryId = "";

    private boolean throwException = false;

    @Override
    public UUID addFeed(final String feedLink, final String feedTitle, final String categoryId) throws ServiceException {
        ++this.callCount;

        if (this.throwException) {
            throw new ServiceException(importJobStartedAlready());
        }

        this.feedLink = feedLink;
        this.feedTitle = feedTitle;
        this.categoryId = categoryId;

        return UUID.randomUUID();
    }

    public void assertCallOnce(final String feedLink, final String feedTitle, final String categoryId) {
        assertEquals(1, this.callCount);
        assertEquals(feedLink, this.feedLink);
        assertEquals(feedTitle, this.feedTitle);
        assertEquals(categoryId, this.categoryId);
    }

    public void assertDidNotCalled() {
        assertEquals(0, this.callCount);
    }

    public void setThrowException(final boolean throwException) {
        this.throwException = throwException;
    }

}
