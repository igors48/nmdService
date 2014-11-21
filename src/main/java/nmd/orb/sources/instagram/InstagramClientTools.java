package nmd.orb.sources.instagram;

import nmd.orb.error.ServiceException;
import nmd.orb.feed.Feed;
import nmd.orb.feed.FeedHeader;
import nmd.orb.feed.FeedItem;
import nmd.orb.sources.instagram.entities.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;
import static nmd.orb.error.ServiceError.*;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.*;
import static nmd.orb.util.StringTools.cutTo;
import static nmd.orb.util.StringTools.trimOrUse;

/**
 * @author : igu
 */
public class InstagramClientTools {

    public static final String NO_DESCRIPTION = "No description";

    private static final String INSTAGRAM_COM = "instagram.com";
    private static final Pattern INSTAGRAM_USER_NAME_PATTERN = Pattern.compile("https?://instagram.com/(.+)", Pattern.CASE_INSENSITIVE);
    private static final String DESCRIPTION_TEMPLATE = "<img src=\"%s\"></img><p>%s</p>";

    public static boolean isItInstagramUrl(final String url) {

        try {

            if (url == null || url.isEmpty()) {
                return false;
            }

            final URI uri = new URI(url);
            final String host = uri.getHost();

            return INSTAGRAM_COM.equalsIgnoreCase(host);
        } catch (URISyntaxException exception) {
            return false;
        }

    }

    public static String getInstagramUserName(final String url) {

        if (!isItInstagramUrl(url)) {
            return null;
        }

        final Matcher matcher = INSTAGRAM_USER_NAME_PATTERN.matcher(url);

        return matcher.find() ? matcher.group(1) : null;
    }

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

    public static Feed convert(final String link, final User user, final ContentEnvelope contentEnvelope, final Date current) throws ServiceException {
        guard(isValidUrl(link));
        guard(notNull(user));
        guard(notNull(contentEnvelope));
        guard(notNull(current));

        final FeedHeader header = convert(link, user);
        final List<FeedItem> items = convert(contentEnvelope, current);

        return new Feed(header, items);
    }

    public static List<FeedItem> convert(final ContentEnvelope contentEnvelope, final Date current) throws ServiceException {
        guard(notNull(contentEnvelope));
        guard(notNull(current));

        assertMetaIsValid(contentEnvelope);

        final List<FeedItem> items = new ArrayList<>();

        final List<Data> dataList = contentEnvelope.data;

        if (dataList == null) {
            throw new ServiceException(instagramNoData());
        }

        for (final Data data : dataList) {
            final FeedItem item = convert(data, current);

            items.add(item);
        }

        return items;
    }

    public static FeedHeader convert(final String link, final User user) {
        guard(isValidUrl(link));
        guard(notNull(user));

        return FeedHeader.create(UUID.randomUUID(), link, user.full_name, user.full_name, link);
    }

    public static FeedItem convert(final Data data, final Date current) throws ServiceException {
        guard(notNull(data));
        guard(notNull(current));

        if (data.link == null) {
            throw new ServiceException(instagramBadDataLink("null"));
        }

        final String link = data.link.trim();

        if (!isValidUrl(link)) {
            throw new ServiceException(instagramBadDataLink(link));
        }

        if (data.type == null) {
            throw new ServiceException(instagramBadDataType("null"));
        }

        final String type = data.type.trim();

        if (!("image".equals(type) || "video".equals(type))) {
            throw new ServiceException(instagramBadDataType(type));
        }

        if (data.images == null) {
            throw new ServiceException(instagramNoImages());
        }

        final Content lowResolutionContent = data.images.low_resolution;
        final Content standardResolutionContent = data.images.standard_resolution;
        final Content thumbnailContent = data.images.thumbnail;

        final String lowResolutionImageUrl = (lowResolutionContent != null && isValidUrl(lowResolutionContent.url)) ? lowResolutionContent.url : "";
        final String standardResolutionImageUrl = (standardResolutionContent != null && isValidUrl(standardResolutionContent.url)) ? standardResolutionContent.url : "";
        final String thumbnailImageUrl = (thumbnailContent != null && isValidUrl(thumbnailContent.url)) ? thumbnailContent.url : "";

        final boolean noImages = lowResolutionImageUrl.isEmpty() && standardResolutionImageUrl.isEmpty() && thumbnailImageUrl.isEmpty();

        if (noImages) {
            throw new ServiceException(instagramNoImages());
        }

        final String imageUrl;

        if (!thumbnailImageUrl.isEmpty()) {
            imageUrl = thumbnailImageUrl;
        } else if (!lowResolutionImageUrl.isEmpty()) {
            imageUrl = lowResolutionImageUrl;
        } else {
            imageUrl = standardResolutionImageUrl;
        }

        final Caption caption = data.caption;
        final String captionText;

        if (caption == null) {
            captionText = NO_DESCRIPTION;
        } else {
            captionText = caption.text == null ? NO_DESCRIPTION : caption.text.trim();
        }

        final String title = cutTo(trimOrUse(captionText, NO_DESCRIPTION), FeedItem.MAX_TITLE_LENGTH);

        final Long createdDate = data.created_time;

        final boolean dateReal;
        final Date date;

        if (createdDate == null) {
            dateReal = false;
            date = current;
        } else {
            dateReal = true;
            date = new Date(createdDate * 1000);
        }

        final String imageWithDescription = formatDescription(imageUrl, title);
        final String itemGuid = UUID.randomUUID().toString();

        return new FeedItem(title, imageWithDescription, link, link, date, dateReal, itemGuid);
    }

    public static String formatDescription(String imageUrl, String description) {
        final int maxDescriptionLength = FeedItem.MAX_TITLE_LENGTH - DESCRIPTION_TEMPLATE.length() - imageUrl.length();
        final String cutDescription = cutTo(description, maxDescriptionLength);

        return format(DESCRIPTION_TEMPLATE, imageUrl, cutDescription);
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
