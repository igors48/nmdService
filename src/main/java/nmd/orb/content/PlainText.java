package nmd.orb.content;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Created by igor on 28.06.2015.
 */
public class PlainText implements ContentElement {

    public final String text;

    public PlainText(final String text) {
        guard(notNull(this.text = text));
    }

}
