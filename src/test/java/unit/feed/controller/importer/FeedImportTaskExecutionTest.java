package unit.feed.controller.importer;

import nmd.orb.reader.Category;
import nmd.orb.services.importer.FeedImportContext;
import nmd.orb.services.importer.FeedImportTaskStatus;
import nmd.orb.services.importer.ImportJob;
import org.junit.Before;
import org.junit.Test;
import unit.feed.controller.stub.FeedsServiceAdapterStub;

import static org.junit.Assert.assertEquals;

/**
 * @author : igu
 */
public class FeedImportTaskExecutionTest {

    private static final String HTTP_DOMAIN_COM = "http://domain.com";
    private static final String TITLE = "title";

    private FeedsServiceAdapterStub feedsServiceAdapterStub;

    @Before
    public void setUp() {
        this.feedsServiceAdapterStub = new FeedsServiceAdapterStub();
    }

    @Test
    public void whenTaskExecutedThenRetiresCountDecreased() {
        final int triesLeft = 5;
        final FeedImportContext context = new FeedImportContext(HTTP_DOMAIN_COM, TITLE, triesLeft, FeedImportTaskStatus.WAITING);

        ImportJob.execute(context, Category.MAIN_CATEGORY_ID, this.feedsServiceAdapterStub);

        assertEquals(triesLeft - 1, context.getTriesLeft());
    }

    @Test
    public void whenTaskExecutedThenAdapterCallsOnceWithCorrectParameters() {
        final FeedImportContext context = new FeedImportContext(HTTP_DOMAIN_COM, TITLE, 5, FeedImportTaskStatus.WAITING);

        ImportJob.execute(context, Category.MAIN_CATEGORY_ID, this.feedsServiceAdapterStub);

        this.feedsServiceAdapterStub.assertCallOnce(HTTP_DOMAIN_COM, TITLE, Category.MAIN_CATEGORY_ID);
    }

    @Test
    public void whenTaskExecutedWithoutErrorsThenItsStatusChangedToCompleted() {
        final FeedImportContext context = new FeedImportContext(HTTP_DOMAIN_COM, TITLE, 5, FeedImportTaskStatus.WAITING);

        ImportJob.execute(context, Category.MAIN_CATEGORY_ID, this.feedsServiceAdapterStub);

        assertEquals(FeedImportTaskStatus.COMPLETED, context.getStatus());
    }

    @Test
    public void whenTaskExecutedWithErrorsAndNoTriesLeftThenItsStatusChangedToFailed() {
        final FeedImportContext context = new FeedImportContext(HTTP_DOMAIN_COM, TITLE, 1, FeedImportTaskStatus.WAITING);

        this.feedsServiceAdapterStub.setThrowException(true);
        ImportJob.execute(context, Category.MAIN_CATEGORY_ID, this.feedsServiceAdapterStub);

        assertEquals(FeedImportTaskStatus.FAILED, context.getStatus());
    }

    @Test
    public void whenTaskExecutedWithErrorsAndThereAreTriesLeftThenItsStatusChangedToFailed() {
        final FeedImportContext context = new FeedImportContext(HTTP_DOMAIN_COM, TITLE, 2, FeedImportTaskStatus.WAITING);

        this.feedsServiceAdapterStub.setThrowException(true);
        ImportJob.execute(context, Category.MAIN_CATEGORY_ID, this.feedsServiceAdapterStub);

        assertEquals(FeedImportTaskStatus.ERROR, context.getStatus());
    }

}
