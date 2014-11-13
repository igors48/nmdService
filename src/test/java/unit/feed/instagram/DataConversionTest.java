package unit.feed.instagram;

import nmd.orb.sources.instagram.entities.Caption;
import nmd.orb.sources.instagram.entities.Content;
import nmd.orb.sources.instagram.entities.Data;
import nmd.orb.sources.instagram.entities.Images;
import org.junit.Before;

/**
 * @author : igu
 */
public class DataConversionTest extends AbstractInstagramTestBase {

    private Data data;

    @Before
    public void setUp() {
        this.data = new Data();

        Caption caption = new Caption();
        caption.text = "caption";

        Images images = new Images();

        Content content = new Content();
        content.url = "";
        content.height = "";
        content.width = "";

        /*
            public String link;
    public String type;
    public Long created_time;
    public Caption caption;
    public Images images;
    public Videos videos;

         */
    }

}
