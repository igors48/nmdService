package nmd.rss.collector.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 12.08.2014
 */
public class Page<T> {

    public final boolean first;
    public final boolean last;
    public final List<T> items;

    public Page(final List<T> list, final int offset, final int size) {
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
