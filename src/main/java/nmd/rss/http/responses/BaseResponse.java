package nmd.rss.http.responses;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.06.13
 */
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class BaseResponse {

    protected ResponseType status = null;

    public ResponseType getStatus() {
        return this.status;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseResponse that = (BaseResponse) o;

        if (status != that.status) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return status != null ? status.hashCode() : 0;
    }

    protected BaseResponse() {
        // empty
    }

}
