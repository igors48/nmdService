package unit.content;

import nmd.orb.content.ContentElement;
import nmd.orb.content.ContentRenderer;
import nmd.orb.content.element.Image;
import nmd.orb.content.element.PlainText;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by igor on 28.06.2015.
 */
@RunWith(value = Parameterized.class)
public class ContentRendererTest {

    private final List<ContentElement> elements;
    private final String content;

    public ContentRendererTest(final List<ContentElement> elements, final String content) {
        this.elements = elements;
        this.content = content;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(
                new Object[][]{
                        {new ArrayList<>(), ""},
                        {Arrays.asList(new PlainText("a")), "<p>a</p>"},
                        {Arrays.asList(new PlainText("a"), new PlainText("b")), "<p>a</p><p>b</p>"},
                        {Arrays.asList(new PlainText("a"), new Image("http://domain.com/abc")), "<p>a</p><img src=\"http://domain.com/abc\"></img>"}
                }
        );
    }

    @Test
    public void transform() {
        final String result = ContentRenderer.render(this.elements);

        assertEquals(this.content, result);
    }

}
