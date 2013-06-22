package nmd.rss.collector.rest;

import nmd.rss.collector.util.Assert;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 22.06.13
 */
class SuccessMessageResponse extends SuccessResponse {

    private String message;

    private SuccessMessageResponse() {
        this("");
    }

    public SuccessMessageResponse(final String message) {
        super();

        setMessage(message);
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(final String message) {
        Assert.assertStringIsValid(message);
        this.message = message;
    }

}
