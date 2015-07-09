package nmd.orb.content.element;

import nmd.orb.content.ContentElement;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlainText plainText = (PlainText) o;

        return text.equals(plainText.text);

    }

    @Override
    public int hashCode() {
        return text.hashCode();
    }

    @Override
    public String toString() {
        return "PlainText{" +
                "text='" + text + '\'' +
                '}';
    }

}
