package nmd.rss.collector.rest;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.06.13
 */
class ErrorResponse extends BaseResponse {

    protected ErrorResponse() {
        super();

        this.responseType = ResponseType.ERROR;
    }

}
