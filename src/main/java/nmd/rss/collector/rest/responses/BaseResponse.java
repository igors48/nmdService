package nmd.rss.collector.rest.responses;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.06.13
 */
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class BaseResponse {

    protected ResponseType status = null;

    protected BaseResponse() {
        // empty
    }

}
