package nmd.orb.content.render;

import nmd.orb.content.ElementRenderer;
import nmd.orb.content.element.Image;

import static java.lang.String.format;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * @author : igu
 */
public class ImageRender implements ElementRenderer<Image> {

    public static final ImageRender IMAGE_RENDER = new ImageRender();

    @Override
    public String render(final Image element) {
        guard(notNull(element));

        return format("<img src=\"%s\" align=\"left\" width=\"100\" style=\"float: left;\" class=\"padding-right\"></img>", element.src);
    }

}
