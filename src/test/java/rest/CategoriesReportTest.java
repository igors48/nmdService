package rest;

import nmd.rss.collector.rest.responses.CategoriesReportResponse;
import nmd.rss.collector.rest.responses.payload.CategoryReportPayload;
import org.junit.Test;

import static nmd.rss.collector.util.Parameter.isPositive;
import static nmd.rss.reader.Category.isValidCategoryId;
import static nmd.rss.reader.Category.isValidCategoryName;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 23.03.2014
 */
public class CategoriesReportTest extends AbstractRestTest {

    @Test
    public void allCategoriesAreReturnedInReport() {
        addFirstFeed();
        addSecondFeed();

        final CategoriesReportResponse response = getCategoriesReport();

        assertEquals(1, response.reports.size());

        final CategoryReportPayload firstReport = response.reports.get(0);

        assertTrue(isValidCategoryId(firstReport.id));
        assertTrue(isValidCategoryName(firstReport.name));
        assertTrue(isPositive(firstReport.feedCount));
        assertTrue(isPositive(firstReport.notRead));
        assertTrue(isPositive(firstReport.read));
        assertTrue(isPositive(firstReport.readLater));
    }
}
