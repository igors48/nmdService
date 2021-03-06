package unit.feed.controller;

import nmd.orb.services.quota.Quota;

import static nmd.orb.util.Assert.assertPositive;

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
    public void start() {
        // empty
    }

    @Override
    public boolean expired() {
        this.count--;

        return this.count < 0;
    }
}
