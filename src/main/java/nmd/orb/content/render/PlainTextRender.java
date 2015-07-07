package nmd.orb.content.render;

import nmd.orb.content.ElementRenderer;
import nmd.orb.content.element.PlainText;

import static java.lang.String.format;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * @author : igu
 */
public class PlainTextRender implements ElementRenderer<PlainText> {

    public static final PlainTextRender PLAIN_TEXT_RENDER = new PlainTextRender();

    @Override
    public String render(final PlainText element) {
        guard(notNull(element));

        return format("<p>%s</p>", element.text);
    }

}
