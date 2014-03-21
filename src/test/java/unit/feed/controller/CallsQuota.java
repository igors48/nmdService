package unit.feed.controller;

import nmd.rss.collector.controller.Quota;

import static nmd.rss.collector.util.Assert.assertPositive;

/**
 * @author : igu
 */
public class CallsQuota implements Quota {

    private int count;

    public CallsQuota(final int count) {
        assertPositive(count);
        this.count = count;
    }

    @Override
    public boolean expired() {
        this.count--;

        return this.count < 0;
    }
}
