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

}
