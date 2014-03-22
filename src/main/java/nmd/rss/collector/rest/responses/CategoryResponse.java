package nmd.rss.collector.rest.responses;

import nmd.rss.reader.Category;

import static nmd.rss.collector.util.Assert.assertNotNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.03.2014
 */
public class CategoryResponse extends SuccessResponse {

    public String name;
    public String id;

    private CategoryResponse() {
        this.name = "";
        this.id = "";
    }

    public static CategoryResponse convert(final Category category) {
        assertNotNull(category);

        final CategoryResponse response = new CategoryResponse();

        response.name = category.name;
        response.id = category.uuid;

        return response;
    }

}
