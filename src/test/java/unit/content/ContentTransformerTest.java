package unit.content;

import nmd.orb.content.ContentElement;
import nmd.orb.content.ContentTransformer;
import org.junit.Test;

import java.util.List;

/**
 * Created by igor on 28.06.2015.
 */
public class ContentTransformerTest {

    @Test
    public void smoke() {
        List<ContentElement> result = ContentTransformer.transform("c<p>d");
    }


}
