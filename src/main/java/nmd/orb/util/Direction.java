package nmd.orb.util;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Created by igor on 06.02.2015.
 */
public enum Direction {

    PREVIOUS("prev"),
    NEXT("next");

    String name;

    Direction(final String name) {
        this.name = name;
    }

    public static Direction forName(final String name) {
        guard(notNull(name));

        switch (name) {
            case "prev": return PREVIOUS;
            case "next": return NEXT;
            default: return null;
        }
    }

}
