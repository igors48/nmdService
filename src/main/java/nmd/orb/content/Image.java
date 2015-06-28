package nmd.orb.content;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.isValidUrl;

/**
 * Created by igor on 28.06.2015.
 */
public class Image implements ContentElement {

    public final String src;

    public Image(final String src) {
        guard(isValidUrl(this.src = src));
    }

}
