package nmd.orb.sources.instagram;

import com.google.gson.Gson;
import nmd.orb.error.ServiceException;
import nmd.orb.feed.Feed;
import nmd.orb.sources.instagram.entities.ContentEnvelope;
import nmd.orb.sources.instagram.entities.User;
import nmd.orb.sources.instagram.entities.UserEnvelope;
import nmd.orb.util.ConnectionTools;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Date;

import static java.lang.String.format;
import static nmd.orb.error.ServiceError.urlFetcherError;
import static nmd.orb.sources.instagram.InstagramClientTools.isItInstagramUrl;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.ConnectionTools.readStringFromConnection;
import static nmd.orb.util.ConnectionTools.setupConnection;
import static nmd.orb.util.Parameter.isValidString;

/**
 * @author : igu
 */
public class InstagramClient {

    public static final String CLIENT_ID = System.getProperty("instagram.clientId");

    private static final Gson GSON = new Gson();

    private static final String RECENT_MEDIA_URL_TEMPLATE = "https://api.instagram.com/v1/users/%s/media/recent/?client_id=%s";
    private static final String SEARCH_USER_URL_TEMPLATE = "https://api.instagram.com/v1/users/search?q=%s&client_id=%s";

    public static Feed fetchAsInstagramUrl(final String instagramUrl) throws ServiceException {
        guard(isItInstagramUrl(instagramUrl));

        try {
            final String userName = InstagramClientTools.getInstagramUserName(instagramUrl);
            final UserEnvelope userEnvelope = InstagramClient.searchUsers(userName, InstagramClient.CLIENT_ID);
            final User user = InstagramClientTools.findUser(userName, userEnvelope);
            final ContentEnvelope recentMedia = InstagramClient.fetchRecentMedia(user.id, InstagramClient.CLIENT_ID);

            return InstagramClientTools.convert(instagramUrl, user, recentMedia, new Date());
        } catch (IOException exception) {
            throw new ServiceException(urlFetcherError(instagramUrl), exception);
        }
    }

    public static UserEnvelope searchUsers(final String mask, final String clientId) throws IOException {
        guard(isValidString(mask));
        guard(isValidString(clientId));

        final String link = format(SEARCH_USER_URL_TEMPLATE, mask, clientId);
        final String content = getContent(link);

        return GSON.fromJson(content, UserEnvelope.class);
    }

    public static ContentEnvelope fetchRecentMedia(final String userId, final String clientId) throws IOException {
        guard(isValidString(userId));
        guard(isValidString(clientId));

        final String link = format(RECENT_MEDIA_URL_TEMPLATE, userId, clientId);
        final String content = getContent(link);

        return GSON.fromJson(content, ContentEnvelope.class);
    }

    private static String getContent(final String link) throws IOException {
        final HttpURLConnection connection = setupConnection(link, ConnectionTools.Method.GET);

        return readStringFromConnection(connection);
    }

}
