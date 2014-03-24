package rest;

import nmd.rss.collector.rest.responses.CategoriesReportResponse;
import nmd.rss.collector.rest.responses.CategoryResponse;
import nmd.rss.collector.rest.responses.payload.CategoryReportPayload;
import nmd.rss.reader.Category;
import org.junit.Test;

import static nmd.rss.reader.Category.MAIN_CATEGORY_ID;
import static org.junit.Assert.assertEquals;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 23.03.2014
 */
public class CategoriesReportTest extends AbstractRestTest {

    private static final String CATEGORY_NAME = "first";

    @Test
    public void allCategoriesAreReturnedInReport() {
        final CategoryResponse categoryResponse = addCategoryWithResponse(CATEGORY_NAME);
        addFeedWithResponse(FIRST_FEED_URL, MAIN_CATEGORY_ID);
        addFeedWithResponse(SECOND_FEED_URL, categoryResponse.category.id);

        final CategoriesReportResponse response = getCategoriesReport();

        assertEquals(2, response.reports.size());

        final CategoryReportPayload firstReport = response.reports.get(0);
        final CategoryReportPayload secondReport = response.reports.get(1);

        assertEquals(categoryResponse.category.id, firstReport.id);
        assertEquals(CATEGORY_NAME, firstReport.name);
        assertEquals(1, firstReport.feedCount);
        assertEquals(100, firstReport.notRead);
        assertEquals(0, firstReport.read);
        assertEquals(0, firstReport.readLater);

        assertEquals(Category.MAIN_CATEGORY_ID, secondReport.id);
        assertEquals(Category.MAIN_CATEGORY_ID, secondReport.name);
        assertEquals(1, secondReport.feedCount);
        assertEquals(100, secondReport.notRead);
        assertEquals(0, secondReport.read);
        assertEquals(0, secondReport.readLater);
    }
}
