package nmd.rss.collector.rest.wrappers;

import nmd.rss.collector.rest.tools.ResponseBody;

import java.util.UUID;

/**
 * @author : igu
 */
public interface CategoriesServiceWrapper {

    ResponseBody addCategory(String name);

    ResponseBody getCategoriesReport();

    ResponseBody getCategoryReport(String categoryId);

    ResponseBody assignFeedToCategory(UUID feedId, String categoryId);

    ResponseBody deleteCategory(String categoryId);

    ResponseBody renameCategory(String categoryId, String newName);

}
