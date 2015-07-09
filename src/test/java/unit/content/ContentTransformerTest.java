package unit.content;

import nmd.orb.content.ContentElement;
import nmd.orb.content.ContentTransformer;
import nmd.orb.content.element.Image;
import nmd.orb.content.element.PlainText;
import nmd.orb.content.element.PlainTextFromATag;
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
public class ContentTransformerTest {

    private static final String DOMAIN2 = "http://domain2.com";

    private final String content;
    private final List<ContentElement> elements;

    public ContentTransformerTest(final String content, final List<ContentElement> elements) {
        this.content = content;
        this.elements = elements;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(
                new Object[][]{
                        {"", new ArrayList<>()},
                        {"c", Arrays.asList(new PlainText("c"))},
                        {"c<!--This is a comment.-->", Arrays.asList(new PlainText("c"))},
                        {"c<p>d", Arrays.asList(new PlainText("c"), new PlainText("d"))},
                        {"c<h1>d", Arrays.asList(new PlainText("c"), new PlainText("d"))},
                        {"c<img>", Arrays.asList(new PlainText("c"))},
                        {"c<img src=\"\">", Arrays.asList(new PlainText("c"))},
                        {"c<img src=\"http://domain.com/abc\">", Arrays.asList(new PlainText("c"), new Image("http://domain.com/abc"))},
                        {"c<img src=\"http://domain.com/abc\">d", Arrays.asList(new PlainText("c"), new Image("http://domain.com/abc"), new PlainText("d"))},
                        {"c<img src=\"/abc\">", Arrays.asList(new PlainText("c"), new Image("http://domain2.com/abc"))},
                        {"<a href=\"#\">a</a>", Arrays.asList(new PlainTextFromATag("a"))},
                        {"a<a href=\"#\">b</a>", Arrays.asList(new PlainText("ab"))},
                        {"a<a href=\"#\">b</a><a href=\"#\">c</a>", Arrays.asList(new PlainText("abc"))},
                        {"c<img src=\"/abc\"><a href=\"#\">b</a>", Arrays.asList(new PlainText("c"), new Image("http://domain2.com/abc"), new PlainTextFromATag("b"))},
                        {"c<img src=\"/abc\"><a href=\"#\">b</a><p>f</p>", Arrays.asList(new PlainText("c"), new Image("http://domain2.com/abc"), new PlainText("bf"))},
                        {"<a href=\"#\">b</a><p>f</p>", Arrays.asList(new PlainText("bf"))}
                }
        );
    }

    @Test
    public void transform() {
        List<ContentElement> result = ContentTransformer.transform(DOMAIN2, this.content);

        assertEquals(this.elements, result);
    }

}
