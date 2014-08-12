package nmd.rss.collector.util;

import java.util.ArrayList;
import java.util.List;

import static nmd.rss.collector.util.Assert.guard;
import static nmd.rss.collector.util.Parameter.isPositive;
import static nmd.rss.collector.util.Parameter.notNull;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 12.08.2014
 */
public class Page<T> {

    public final boolean first;
    public final boolean last;
    public final List<T> items;

    public Page(final List<T> list, final int offset, final int size) {
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

        this.first = first;
        this.last = last;
        this.items = offset > list.size() ? new ArrayList<T>() : list.subList(offset, lastIndex);
    }

}
