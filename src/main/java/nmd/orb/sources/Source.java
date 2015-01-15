package nmd.orb.sources;

import nmd.orb.sources.instagram.InstagramClientTools;
import nmd.orb.sources.twitter.TwitterClientTools;

import static nmd.orb.util.Parameter.isValidUrl;

/**
 * @author : igu
 */
public enum Source {

    RSS,
    TWITTER,
    INSTAGRAM;

    public static Source detect(final String url) {

        if (!isValidUrl(url)) {
            return null;
        }

        if (TwitterClientTools.isItTwitterUrl(url)) {
            return TWITTER;
        }

        if (InstagramClientTools.isItInstagramUrl(url)) {
            return INSTAGRAM;
        }

        return RSS;
    }

}
