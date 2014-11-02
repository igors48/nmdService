package nmd.orb.http;

import nmd.orb.http.tools.ResponseBody;

import java.util.List;
import java.util.Map;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

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
