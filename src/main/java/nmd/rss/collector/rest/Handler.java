package nmd.rss.collector.rest;

import nmd.rss.collector.rest.tools.ResponseBody;

import java.util.List;
import java.util.Map;

import static nmd.rss.collector.util.Assert.guard;
import static nmd.rss.collector.util.Parameter.notNull;

/**
 * @author : igu
 */
public interface Handler {

    ResponseBody handle(List<String> elements, Map<String, String> parameters, String body);

    static final Handler EMPTY_HANDLER = new Handler() {

        @Override
        public ResponseBody handle(final List<String> elements, final Map<String, String> parameters, final String body) {
            guard(notNull(elements));
            guard(notNull(parameters));
            guard(notNull(body));

            return null;
        }
    };

}
