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

    public static <T> Page<T> create(final List<FeedItem> list, final String id, final int size, final boolean forward) {
        guard(notNull(list));
        guard(isValidFeedItemGuid(id));
        guard(isPositive(size));

        //return new Page<T>(items, first, last);

        return null;
    }

}
