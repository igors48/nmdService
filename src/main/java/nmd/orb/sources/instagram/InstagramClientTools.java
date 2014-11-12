package nmd.orb.sources.instagram;

import nmd.orb.error.ServiceException;
import nmd.orb.feed.FeedItem;
import nmd.orb.sources.instagram.entities.Data;
import nmd.orb.sources.instagram.entities.Envelope;
import nmd.orb.sources.instagram.entities.User;
import nmd.orb.sources.instagram.entities.UserEnvelope;

import java.util.List;

import static nmd.orb.error.ServiceError.*;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.*;

/**
 * @author : igu
 */
public class InstagramClientTools {

    public static User findUser(final String userName, final UserEnvelope userEnvelope) throws ServiceException {
        guard(isValidString(userName));
        guard(notNull(userEnvelope));

        assertMetaIsValid(userEnvelope);

        final List<User> users = userEnvelope.data;

        if (users == null || users.isEmpty()) {
            throw new ServiceException(instagramNoUsers());
        }

        for (User user : users) {

            if (user.username.equals(userName)) {
                return user;
            }
        }

        throw new ServiceException(instagramUserNotFound(userName));
    }

    public static FeedItem convert(final Data data) {
        guard(notNull(data));

        if (data.link == null) {

        }

        final String link = data.link.trim();

        if (!isValidUrl(link)) {

        }

        final String type = data.type.trim();

        if (!("image".equals(type) || "video".equals(type))) {

        }

        final String lowResolutionImage = data.images.low_resolution.url;
        /*
            public String link;
            public String type;
            public Long created_time;
            public Caption caption;
            public Images images;
            public Videos videos;
         */

        /*
            public final String title;
            public final String description;
            public final String link;
            public final String gotoLink;
            public final Date date;
            public final boolean dateReal;
            public final String guid;
         */

        return null;
    }

    private static void assertMetaIsValid(Envelope envelope) throws ServiceException {

        if (envelope.meta == null) {
            throw new ServiceException(instagramNoMeta());
        }

        if (!"200".equals(envelope.meta.code)) {
            throw new ServiceException(instagramWrongStatusCode());
        }

    }

}
