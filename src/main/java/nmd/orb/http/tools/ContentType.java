package nmd.orb.http.tools;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 24.06.13
 */
public enum ContentType {

    XML("application/orb+xml"),
    JSON("application/json");

    public final String mime;

    private ContentType(final String mime) {
        this.mime = mime;
    }

}
