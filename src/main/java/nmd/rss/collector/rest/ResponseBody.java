package nmd.rss.collector.rest;

import static nmd.rss.collector.util.Assert.assertNotNull;
import static nmd.rss.collector.util.Assert.assertStringIsValid;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 24.06.13
 */
public class ResponseBody {

    public final ContentType contentType;
    public final String content;

    public ResponseBody(final ContentType contentType, final String content) {
        assertNotNull(contentType);
        this.contentType = contentType;

        assertStringIsValid(content);
        this.content = content;
    }

}
