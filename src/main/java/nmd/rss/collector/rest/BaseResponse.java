package nmd.rss.collector.rest;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.06.13
 */
abstract class BaseResponse {

    protected ResponseType status;

    protected BaseResponse() {
        this.status = null;
    }

    ResponseType getStatus() {
        return this.status;
    }

    void setStatus(final ResponseType status) {
        this.status = status;
    }

}
