package nmd.rss.collector.rest.responses.payload;

import nmd.rss.reader.Category;

import static nmd.rss.collector.util.Assert.guard;
import static nmd.rss.collector.util.Parameter.notNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.03.2014
 */
public class CategoryPayload {

    public String name;
    public String id;

    public CategoryPayload(final Category category) {
        guard(notNull(category));
        this.name = category.name;
        this.id = category.uuid;
    }

}
