package nmd.orb.content.element;

import nmd.orb.content.ContentElement;
import nmd.orb.http.servlets.ReadsServlet;

import java.util.logging.Level;
import java.util.logging.Logger;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.isValidUrl;

/**
 * Created by igor on 28.06.2015.
 */
public class Image implements ContentElement {

    private static final Logger LOGGER = Logger.getLogger(Image.class.getName());

    public final String src;

    public Image(final String src) {
        this.src = src;

        //guard(isValidUrl(this.src = src));

        if (!isValidUrl(src)) {
            LOGGER.log(Level.SEVERE, String.format("Invalid image src [ %s ]", src));
        }
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
