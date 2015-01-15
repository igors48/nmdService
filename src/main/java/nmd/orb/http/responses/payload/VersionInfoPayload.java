package nmd.orb.http.responses.payload;

/**
 * @author : igu
 */
public class VersionInfoPayload {

    public String major;
    public String minor;
    public String sha1;

    private VersionInfoPayload() {
        // empty
    }

    public static VersionInfoPayload create() {
        final VersionInfoPayload versionInfoPayload = new VersionInfoPayload();

        final String major = System.getProperty("application.version.major");
        final String minor = System.getProperty("application.version.minor");
        final String sha1 = System.getProperty("git.sha");

        versionInfoPayload.major = major == null ? "" : major;
        versionInfoPayload.minor = minor == null ? "" : minor;
        versionInfoPayload.sha1 = sha1 == null ? "" : sha1;

        return versionInfoPayload;
    }

}
