package nmd.rss.collector.rest;

import nmd.rss.collector.feed.FeedHeader;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

import static nmd.rss.collector.error.ServiceError.*;
import static nmd.rss.collector.rest.CategoriesServiceWrapper.*;
import static nmd.rss.collector.rest.ResponseBody.createErrorJsonResponse;
import static nmd.rss.collector.rest.ServletTools.parse;
import static nmd.rss.collector.rest.ServletTools.readRequestBody;
import static nmd.rss.reader.Category.isValidCategoryId;
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

    // DELETE -- /{categoryId} delete category
    @Override
    protected ResponseBody handleDelete(final HttpServletRequest request) {
        final String pathInfo = request.getPathInfo();

        final List<String> elements = parse(pathInfo);

        if (elements.isEmpty()) {
            return createErrorJsonResponse(invalidCategoryId(""));
        }

        final String first = elements.get(0);

        return isValidCategoryId(first) ? deleteCategory(first) : createErrorJsonResponse(invalidCategoryId(first));
    }

    // PUT -- /{categoryId} rename category
    // PUT -- /{categoryId}/{feedId} assign feed to category
    @Override
    protected ResponseBody handlePut(final HttpServletRequest request) {
        final String pathInfo = request.getPathInfo();
        final List<String> elements = parse(pathInfo);

        if (elements.isEmpty()) {
            return createErrorJsonResponse(invalidCategoryId(""));
        }

        final String categoryId = elements.get(0);

        if (!isValidCategoryId(categoryId)) {
            return createErrorJsonResponse(invalidCategoryId(categoryId));
        }

        if (elements.size() == 1) {
            final String categoryName = readRequestBody(request);

            if (!isValidCategoryName(categoryName)) {
                return createErrorJsonResponse(invalidCategoryName(categoryName));
            }

            return renameCategory(categoryId, categoryName);
        } else {
            final String feedId = elements.get(1);

            if (!FeedHeader.isValidFeedId(feedId)) {
                return createErrorJsonResponse(invalidFeedId(feedId));
            }

            return assignFeedToCategory(UUID.fromString(feedId), categoryId);
        }
    }

}
