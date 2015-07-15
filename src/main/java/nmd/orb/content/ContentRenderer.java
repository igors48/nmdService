package nmd.orb.content;

import nmd.orb.content.element.Image;
import nmd.orb.content.element.PlainText;
import nmd.orb.content.element.PlainTextFromATag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nmd.orb.content.render.ImageRender.IMAGE_RENDER;
import static nmd.orb.content.render.PlainTextRender.PLAIN_TEXT_RENDER;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * @author : igu
 */
public class ContentRenderer {

    private static final Map<Class<? extends ContentElement>, ElementRenderer> RENDERERS;

    static {
        RENDERERS = new HashMap<>();

        RENDERERS.put(Image.class, IMAGE_RENDER);
        RENDERERS.put(PlainText.class, PLAIN_TEXT_RENDER);
        RENDERERS.put(PlainTextFromATag.class, PLAIN_TEXT_RENDER);
    }

    public static String render(final List<ContentElement> elements) {
        guard(notNull(elements));

        final StringBuilder result = new StringBuilder();

        boolean oneImageRendered = false;

        for (final ContentElement element : elements) {
            boolean isItImage = element instanceof Image;

            if (isItImage && oneImageRendered) {
                continue;
            }

            final ElementRenderer renderer = RENDERERS.get(element.getClass());

            if (renderer != null) {
                final String content = renderer.render(element);

                result.append(content);
            }

            if (isItImage) {
                oneImageRendered = true;
            }
        }

        return result.toString();
    }

}
