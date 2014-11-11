package nmd.orb.services.filter;

import nmd.orb.services.report.FeedItemReport;

import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.notNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 */
public enum FeedItemReportFilter {

    SHOW_ALL("show-all") {
        @Override
        public boolean acceptable(final FeedItemReport candidate) {
            guard(notNull(candidate));

            return true;
        }
    },

    SHOW_NOT_READ("show-not-read") {
        @Override
        public boolean acceptable(final FeedItemReport candidate) {
            guard(notNull(candidate));

            return !candidate.read;
        }
    },

    SHOW_ADDED("show-added") {
        @Override
        public boolean acceptable(final FeedItemReport candidate) {
            guard(notNull(candidate));

            return candidate.addedSinceLastView;
        }
    },

    SHOW_READ_LATER("show-read-later") {
        @Override
        public boolean acceptable(final FeedItemReport candidate) {
            guard(notNull(candidate));

            return candidate.readLater;
        }
    };

    private FeedItemReportFilter(final String name) {
        this.name = name;
    }

    String name;

    public abstract boolean acceptable(final FeedItemReport candidate);

    public static FeedItemReportFilter forName(final String name) {
        guard(notNull(name));

        switch (name) {
            case "show-all":
                return SHOW_ALL;
            case "show-added":
                return SHOW_ADDED;
            case "show-not-read":
                return SHOW_NOT_READ;
            case "show-read-later":
                return SHOW_READ_LATER;
            default:
                return SHOW_ALL;
        }
    }
}
