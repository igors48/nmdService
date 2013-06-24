package nmd.rss.collector.rest;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.06.13
 */
abstract class SuccessResponse extends BaseResponse {

    protected SuccessResponse() {
        super();

        this.status = ResponseType.SUCCESS;
    }

}
