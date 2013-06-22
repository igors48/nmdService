package nmd.rss.collector.rest;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.06.13
 */
abstract class BaseResponse {

    protected ResponseType responseType;

    protected BaseResponse() {
        this.responseType = null;
    }

    ResponseType getResponseType() {
        return this.responseType;
    }

    void setResponseType(final ResponseType responseType) {
        this.responseType = responseType;
    }

}
