package sandbox;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 12.11.2014
 */
public class ContentEnvelope {

    public Meta meta = new Meta();
    public List<Data> data = new ArrayList<>();
    public Pagination pagination = new Pagination();

}
