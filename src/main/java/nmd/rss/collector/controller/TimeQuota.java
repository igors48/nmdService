package nmd.rss.collector.controller;

import static java.lang.System.currentTimeMillis;
import static nmd.rss.collector.util.Assert.assertPositive;

/**
 * @author : igu
 */
public class TimeQuota implements Quota {

    private final long startTime;
    private final long period;

    public TimeQuota(final long period) {
        assertPositive(period);
        this.period = period;

        this.startTime = currentTimeMillis();
    }

    @Override
    public boolean expired() {
        final long currentTime = currentTimeMillis();

        return currentTime - this.startTime > this.period;
    }

}
