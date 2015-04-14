package nmd.orb.gae.repositories.datastore;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 20.03.2014
 */
public enum RootKind {

    FEED("Feed"),
    CATEGORY("Category"),
    IMPORT("Import"),
    CHANGE("Change");

    public final String value;

    private RootKind(String value) {
        this.value = value;
    }

}
