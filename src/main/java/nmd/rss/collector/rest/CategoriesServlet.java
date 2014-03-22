package nmd.rss.collector.rest;

import javax.servlet.http.HttpServletRequest;

import static nmd.rss.collector.error.ServiceError.invalidCategoryName;
import static nmd.rss.collector.rest.CategoriesServiceWrapper.addCategory;
import static nmd.rss.collector.rest.ResponseBody.createErrorJsonResponse;
import static nmd.rss.collector.rest.ServletTools.readRequestBody;
import static nmd.rss.reader.Category.isValidCategoryName;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.03.2014
 */
public class CategoriesServlet extends AbstractRestServlet {

    // POST -- add category
    @Override
    protected ResponseBody handlePost(final HttpServletRequest request) {
        final String name = readRequestBody(request);

        return (isValidCategoryName(name)) ? addCategory(name) : createErrorJsonResponse(invalidCategoryName(name));
    }

    // GET -- get categories report
    @Override
    protected ResponseBody handleGet(final HttpServletRequest request) {
        return CategoriesServiceWrapper.getCategoriesReport();
    }

}
