package nmd.orb.content.element;

import nmd.orb.content.ContentElement;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Image image = (Image) o;

        return src.equals(image.src);

    }

    @Override
    public int hashCode() {
        return src.hashCode();
    }

}
