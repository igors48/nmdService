package nmd.orb.sources.twitter.entities;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date: 26.02.14
 */
public class AccessToken {

    private String access_token;
    private String token_type;

    public AccessToken() {
        this.access_token = "";
        this.token_type = "";
    }

    public String getAccess_token() {
        return this.access_token;
    }

    public String getToken_type() {
        return this.token_type;
    }

}
