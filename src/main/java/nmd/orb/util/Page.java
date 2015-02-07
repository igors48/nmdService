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

    public final boolean first;
    public final boolean last;
    public final List<T> items;

    public Page(final List<T> items, final boolean first, final boolean last) {
        guard(notNull(items));

        this.first = first;
        this.last = last;
        this.items = items;
    }

    public static <T> Page<T> create(final List<T> list, final int offset, final int size) {
        guard(notNull(list));
        guard(isPositive(offset));
        guard(isPositive(size));

        final boolean first = offset == 0;
        final boolean last;
        final int lastIndex;

        if (offset + size >= list.size()) {
            lastIndex = list.size();
            last = true;
        } else {
            lastIndex = offset + size;
            last = false;
        }

        final List<T> items = offset > list.size() ? new ArrayList<T>() : list.subList(offset, lastIndex);

        return new Page<T>(items, first, last);
    }

    public static Page<FeedItem> create(final List<FeedItem> list, final String keyItemGuid, final int size, final Direction direction) {
        guard(notNull(list));
        guard(isValidFeedItemGuid(keyItemGuid));
        guard(isPositive(size));
        guard(notNull(direction));

        final int keyItemIndex = find(list, keyItemGuid);

        final boolean noKeyItemInList = keyItemIndex == -1;

        if (noKeyItemInList) {
            return new Page<>(new ArrayList<FeedItem>(), true, true);
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
