package rest.categories;

import nmd.rss.collector.error.ErrorCode;
import nmd.rss.collector.rest.responses.CategoriesReportResponse;
import nmd.rss.collector.rest.responses.CategoryReportResponse;
import nmd.rss.collector.rest.responses.CategoryResponse;
import nmd.rss.collector.rest.responses.payload.CategoryReportPayload;
import nmd.rss.reader.Category;
import org.junit.Test;
import rest.AbstractRestTest;

import java.util.UUID;

import static nmd.rss.reader.Category.MAIN_CATEGORY_ID;
import static org.junit.Assert.assertEquals;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 23.03.2014
 */
public class CategoriesReportTest extends AbstractRestTest {

    private static final String CATEGORY_NAME = "first";

    @Test
    public void whenCategoryIdIsNotGivenThenAllCategoriesAreReturnedInReport() {
        final CategoryResponse categoryResponse = addCategoryWithResponse(CATEGORY_NAME);
        addFeedWithResponse(FIRST_FEED_URL, MAIN_CATEGORY_ID);
        addFeedWithResponse(SECOND_FEED_URL, categoryResponse.category.id);

        final CategoriesReportResponse response = getCategoriesReport();

        assertEquals(2, response.reports.size());

        final CategoryReportPayload mainCategoryReport = response.reports.get(response.reports.get(0).name.equals(Category.MAIN_CATEGORY_ID) ? 0 : 1);
        final CategoryReportPayload secondCategoryReport = response.reports.get(response.reports.get(0).name.equals(Category.MAIN_CATEGORY_ID) ? 1 : 0);

        assertEquals(categoryResponse.category.id, secondCategoryReport.id);
        assertEquals(CATEGORY_NAME, secondCategoryReport.name);
        assertEquals(1, secondCategoryReport.feedCount);
        assertEquals(100, secondCategoryReport.notRead);
        assertEquals(0, secondCategoryReport.read);
        assertEquals(0, secondCategoryReport.readLater);

        assertEquals(Category.MAIN_CATEGORY_ID, mainCategoryReport.id);
        assertEquals(Category.MAIN_CATEGORY_ID, mainCategoryReport.name);
        assertEquals(1, mainCategoryReport.feedCount);
        assertEquals(100, mainCategoryReport.notRead);
        assertEquals(0, mainCategoryReport.read);
        assertEquals(0, mainCategoryReport.readLater);
    }

    @Test
    public void whenCategoryIdIsInvalidThenErrorReturns() {
        final String response = getCategoryReportAsString("*1234");

        assertErrorResponse(response, ErrorCode.INVALID_CATEGORY_ID);
    }

    @Test
    public void whenCategoryIdIsNotFoundThenErrorReturns() {
        final String response = getCategoryReportAsString(UUID.randomUUID().toString());

        assertErrorResponse(response, ErrorCode.WRONG_CATEGORY_ID);
    }

    @Test
    public void whenCategoryIdIsFoundThenCategoryExtendedReportReturns() {
        final CategoryResponse categoryResponse = addCategoryWithResponse(CATEGORY_NAME);
        addFeedWithResponse(FIRST_FEED_URL, MAIN_CATEGORY_ID);
        addFeedWithResponse(SECOND_FEED_URL, categoryResponse.category.id);

        final CategoryReportResponse categoryReportResponse = getCategoryReport(categoryResponse.category.id);

        assertEquals(1, categoryReportResponse.report.feedReports.size());
    }
}
