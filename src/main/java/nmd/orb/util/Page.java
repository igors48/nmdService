package nmd.orb.util;

import nmd.orb.feed.FeedItem;

import java.util.ArrayList;
import java.util.List;

import static nmd.orb.feed.FeedItem.isValidFeedItemGuid;
import static nmd.orb.util.Assert.guard;
import static nmd.orb.util.Parameter.isPositive;
import static nmd.orb.util.Parameter.notNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 12.08.2014
 */
public class Page<T> {

    private static final Page<FeedItem> EMPTY_FEED_ITEM_PAGE = new Page<>(new ArrayList<FeedItem>(), true, true);

    public final boolean first;
    public final boolean last;
    public final List<T> items;

    public Page(final List<T> items, final boolean first, final boolean last) {
        guard(notNull(items));

        this.first = first;
        this.last = last;
        this.items = items;
    }

    public static Page<FeedItem> create(final List<FeedItem> list, final String keyItemGuid, final int size, final Direction direction) {
        guard(notNull(list));
        guard(isValidKeyItemGuid(keyItemGuid));
        guard(isPositive(size));
        guard(notNull(direction));

        if (list.isEmpty()) {
            return EMPTY_FEED_ITEM_PAGE;
        }

        final int keyItemIndex = keyItemGuid.isEmpty() ? 0 : find(list, keyItemGuid);

        final boolean noKeyItemInList = keyItemIndex == -1;

        if (noKeyItemInList) {
            return EMPTY_FEED_ITEM_PAGE;
        }

        final int maxIndex = list.size() - 1;

        final boolean first = keyItemIndex == 0;
        final boolean last = keyItemIndex == maxIndex;

        final int fromIndex = direction.equals(Direction.NEXT) ? keyItemIndex : keyItemIndex - size;
        final int fromIndexAdjusted = fromIndex < 0 ? 0 : fromIndex;

        final int toIndex = direction.equals(Direction.NEXT) ? keyItemIndex + size : keyItemIndex;
        final int toIndexAdjusted = (toIndex > maxIndex ? maxIndex : toIndex) + 1;

        final List<FeedItem> subList = list.subList(fromIndexAdjusted, toIndexAdjusted);

        return new Page<>(subList, first, last);
    }

    public static boolean isValidKeyItemGuid(final String keyItemGuid) {
        return notNull(keyItemGuid) && (keyItemGuid.isEmpty() || isValidFeedItemGuid(keyItemGuid));
    }

    private static int find(final List<FeedItem> list, final String guid) {

        for (int index = 0; index < list.size(); ++index) {
            final FeedItem candidate = list.get(index);

            if (candidate.guid.equals(guid)) {
                return index;
            }
        }

        return -1;
    }

}
