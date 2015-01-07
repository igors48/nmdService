package nmd.orb.services.quota;

/**
 * @author : igu
 */
public interface Quota {

    void start();

    boolean expired();

}
