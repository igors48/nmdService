package http.categories;

import http.AbstractHttpTest;
import nmd.orb.error.ErrorCode;
import nmd.orb.http.responses.CategoriesReportResponse;
import nmd.orb.http.responses.CategoryReportResponse;
import nmd.orb.http.responses.CategoryResponse;
import nmd.orb.http.responses.payload.CategoryReportPayload;
import nmd.orb.reader.Category;
import org.junit.Test;

import java.util.UUID;

import static nmd.orb.reader.Category.MAIN_CATEGORY_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 23.03.2014
 */
public class CategoriesReportTest extends AbstractHttpTest {

    private static final String CATEGORY_NAME = "first";

    @Test
    public void whenCategoryIdIsNotGivenThenAllCategoriesAreReturnedInLightReport() {
        final CategoryResponse categoryResponse = addCategoryWithResponse(CATEGORY_NAME);
        addFeedWithResponse(FIRST_FEED_URL, MAIN_CATEGORY_ID);
        addFeedWithResponse(SECOND_FEED_URL, categoryResponse.category.id);

        final CategoriesReportResponse response = getCategoriesReport();

        assertEquals(2, response.reports.size());

        final CategoryReportPayload mainCategoryReport = response.reports.get(response.reports.get(0).name.equals(Category.MAIN_CATEGORY_ID) ? 0 : 1);
        final CategoryReportPayload secondCategoryReport = response.reports.get(response.reports.get(0).name.equals(Category.MAIN_CATEGORY_ID) ? 1 : 0);

        assertEquals(categoryResponse.category.id, secondCategoryReport.id);
        assertEquals(CATEGORY_NAME, secondCategoryReport.name);
        assertEquals(0, secondCategoryReport.feedCount);
        assertEquals(0, secondCategoryReport.notRead);
        assertEquals(0, secondCategoryReport.read);
        assertEquals(0, secondCategoryReport.readLater);

        assertEquals(Category.MAIN_CATEGORY_ID, mainCategoryReport.id);
        assertEquals(Category.MAIN_CATEGORY_ID, mainCategoryReport.name);
        assertEquals(0, mainCategoryReport.feedCount);
        assertEquals(0, mainCategoryReport.notRead);
        assertEquals(0, mainCategoryReport.read);
        assertEquals(0, mainCategoryReport.readLater);
    }

    @Test
    public void whenCategoryIdIsNotGivenThenApplicationVersionReturnedInReport() {
        final CategoryResponse categoryResponse = addCategoryWithResponse(CATEGORY_NAME);
        addFeedWithResponse(FIRST_FEED_URL, MAIN_CATEGORY_ID);
        addFeedWithResponse(SECOND_FEED_URL, categoryResponse.category.id);

        final CategoriesReportResponse response = getCategoriesReport();

        assertNotNull(response.version);
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
